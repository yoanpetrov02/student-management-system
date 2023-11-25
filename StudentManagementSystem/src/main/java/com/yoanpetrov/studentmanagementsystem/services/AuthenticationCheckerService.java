package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationCheckerService {

    private final UserAccountService userAccountService;

    /**
     * Checks whether the currently authenticated {@code UserAccount}'s {@code User} has the same id as the given userId.
     *
     * @param userId the id to check.
     * @return true if they match, false otherwise.
     */
    public boolean doUserIdsMatch(Long userId) {
        log.debug("Verifying that the user is trying to modify their own information. (only for non-admins)");
        UserAccount account = getCurrentlyAuthenticatedAccount();
        if (account == null) {
            return false;
        }
        return account.getUser().getUserId().equals(userId);
    }

    /**
     * Checks whether the currently authenticated {@code UserAccount}'s {@code User} is enrolled in the given course.
     *
     * @param courseId the id of the course.
     * @return true if they are enrolled, false otherwise
     */
    public boolean isUserEnrolledInCourse(Long courseId) {
        log.debug("Verifying that the user is trying to modify a course that they are in.");
        UserAccount account = getCurrentlyAuthenticatedAccount();
        if (account == null) {
            return false;
        }
        return account.getUser().getCourses()
            .stream()
            .anyMatch((course) ->
                course.getCourseId().equals(courseId));
    }

    /**
     * Returns the {@code UserAccount} of the currently authenticated user.
     *
     * @return the account, or null if there is no authentication or the user was not found.
     */
    private UserAccount getCurrentlyAuthenticatedAccount() {
        Authentication authentication = SecurityContextHolder.createEmptyContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        String username = authentication.getName();
        try {
            return userAccountService.getUserAccountByUsername(username);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }
}
