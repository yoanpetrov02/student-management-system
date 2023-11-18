package com.yoanpetrov.studentmanagementsystem.controllers;

import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
import com.yoanpetrov.studentmanagementsystem.mappers.UserAccountMapper;
import com.yoanpetrov.studentmanagementsystem.services.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/accounts")
@PreAuthorize("hasRole('ADMIN')")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    private final UserAccountService userAccountService;
    private final UserAccountMapper userAccountMapper;

    /**
     * Gets all user accounts.
     *
     * @return 204 if there are no existing accounts,
     * 200 and the account list if there is at least 1 account.
     */
    @GetMapping
    public ResponseEntity<?> getAllAccounts() {
        LOG.debug("Getting all user accounts");
        List<UserAccount> accounts = userAccountService.getAllUserAccounts();
        if (accounts.isEmpty()) {
            LOG.debug("No existing user accounts, returning 204");
            return new ResponseEntity<>("No existing user accounts", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    /**
     * Gets a user account by its id.
     *
     * @param id the id of the account.
     * @return 404 if the account was not found,
     * 200 and the account if it was found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> getAccountById(@PathVariable Long id) {
        LOG.debug("Getting account with id {}", id);
        UserAccount account = userAccountService.getUserAccountById(id);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    /**
     * Creates the given {@code UserAccount}.
     *
     * @param userAccount the account to be created.
     * @return 200 and the created account if it was successfully created.
     */
    @PostMapping
    public ResponseEntity<UserAccount> createUserAccount(@Valid @RequestBody UserAccount userAccount) {
        LOG.debug("Creating new user account");
        UserAccount createdAccount = userAccountService.createUserAccount(userAccount);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    /**
     * Sets a {@code UserAccount}'s associated {@code User}.
     *
     * @param userAccountId the id of the account.
     * @param userId        the id of the {@code User} to be set as the account's user.
     * @return 201 with the user if the action was successful,
     * 404 if the user or the account weren't found.
     */
    @PostMapping("/{userAccountId}/user/{userId}")
    public ResponseEntity<User> setAccountUser(
        @PathVariable Long userAccountId,
        @PathVariable Long userId
    ) {
        LOG.debug("Setting the user of account with id {}", userAccountId);
        User user = userAccountService.setAccountUser(userAccountId, userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Updates the user account with the given id with the new account details.
     *
     * @param id                    the id of the existing account.
     * @param userAccountDetails the new details of the account.
     * @return 200 and the changed account if the action was successful,
     * 404 if the account was not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAccount> updateUserAccount(
        @PathVariable Long id,
        @RequestBody UserAccount userAccountDetails
    ) {
        LOG.debug("Updating user account with id {}", id);
        UserAccount userAccount = userAccountService.updateUserAccount(
            id, userAccountDetails);
        return new ResponseEntity<>(userAccount, HttpStatus.OK);
    }

    /**
     * Deletes all existing user accounts.
     *
     * @return 200 and a message if the deletion was successful.
     */
    @DeleteMapping
    public ResponseEntity<String> deleteAllUserAccounts() {
        LOG.debug("Deleting all user accounts");
        userAccountService.deleteAllUserAccounts();
        return new ResponseEntity<>("All user accounts have been deleted successfully", HttpStatus.OK);
    }

    /**
     * Deletes a user account by its id.
     *
     * @param id the id of the account to be deleted.
     * @return 200 and a message if the deletion was successful,
     * 404 if the account was not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserAccount(@PathVariable Long id) {
        LOG.debug("Deleting user account with id {}", id);
        userAccountService.deleteUserAccount(id);
        return new ResponseEntity<>("User account deleted successfully", HttpStatus.OK);
    }
}
