package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
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
    public boolean isTheSameAsAuthenticatedUser(Long userId) {
        log.debug("Verifying that the user is trying to modify their own information. (only for non-admins)");
        Authentication authentication = SecurityContextHolder.createEmptyContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        String username = authentication.getName();
        UserAccount account = userAccountService.getUserAccountByUsername(username);

        return account.getUser().getUserId().equals(userId);
    }
}
