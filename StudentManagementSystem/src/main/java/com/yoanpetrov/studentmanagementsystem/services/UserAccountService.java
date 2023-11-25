package com.yoanpetrov.studentmanagementsystem.services;

import com.yoanpetrov.studentmanagementsystem.entities.User;
import com.yoanpetrov.studentmanagementsystem.entities.UserAccount;
import com.yoanpetrov.studentmanagementsystem.exceptions.ResourceNotFoundException;
import com.yoanpetrov.studentmanagementsystem.repositories.UserAccountRepository;
import com.yoanpetrov.studentmanagementsystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * User account details service. Used to load a user account by its username from the database.
 */
@RequiredArgsConstructor
@Service
public class UserAccountService implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserAccountService.class);

    private final UserAccountRepository accountRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Loads the user from the database.
     *
     * @param username the username identifying the user whose data is required.
     * @return the {@code UserDetails} object if the user exists.
     * @throws UsernameNotFoundException if a user with that username was not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.debug("Loading user by username: {}", username);
        Optional<UserAccount> accountFromDb = accountRepository.findByUsername(username);
        if (accountFromDb.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        UserAccount account = accountFromDb.get();

        return new org.springframework.security.core.userdetails.User(
            account.getUsername(),
            account.getPassword(),
            true, true, true, true,
            account.getRole().getAuthorities());
    }

    /**
     * Gets all existing user accounts in the database.
     *
     * @return a list of the accounts, empty if no accounts exist.
     */
    public List<UserAccount> getAllUserAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Gets a single user account from the database by its username.
     *
     * @param username the username of the account.
     * @return the account, if it exists.
     * @throws ResourceNotFoundException if the account was not found.
     */
    public UserAccount getUserAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User account not found"));
    }

    /**
     * Gets a single user account from the database by its id.
     *
     * @param id the id of the account.
     * @return the account, if it exists.
     * @throws ResourceNotFoundException if the account was not found.
     */
    public UserAccount getUserAccountById(Long id) {
        return accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User account not found"));
    }

    /**
     * Inserts the given user account into the database.
     *
     * @param userAccount the account to be created.
     * @return the saved account in the database.
     */
    public UserAccount createUserAccount(UserAccount userAccount) {
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        return accountRepository.save(userAccount);
    }

    /**
     * Sets the given {@code UserAccount}'s {@code User}.
     *
     * @param userAccountId the id of the account.
     * @param userId        the id of the {@code User} to be set as the account's user.
     * @return the set {@code User} if the action was successful.
     * @throws ResourceNotFoundException if the user or the account were not found.
     */
    public User setAccountUser(Long userAccountId, Long userId) {
        UserAccount userAccount = accountRepository.findById(userAccountId)
            .orElseThrow(() -> new ResourceNotFoundException("User account not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userAccount.setUser(user);
        accountRepository.save(userAccount);
        return user;
    }

    /**
     * Updates the user account with the given id with the new user account details.
     *
     * @param id                 the id of the account.
     * @param userAccountDetails the new account details.
     * @return the updated account.
     * @throws ResourceNotFoundException if the account was not found.
     */
    public UserAccount updateUserAccount(Long id, UserAccount userAccountDetails) {
        UserAccount userAccount = accountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User account not found"));
        userAccount.setUsername(userAccountDetails.getUsername());
        userAccount.setPassword(passwordEncoder.encode(userAccountDetails.getPassword()));
        userAccount.setRole(userAccountDetails.getRole());

        return accountRepository.save(userAccount);
    }

    /**
     * Deletes all user accounts.
     */
    public void deleteAllUserAccounts() {
        accountRepository.deleteAll();
    }

    /**
     * Deletes a user account by its id.
     *
     * @param id the id of the account.
     * @throws ResourceNotFoundException if the account was not found.
     */
    public void deleteUserAccount(Long id) {
        if (!existsUserAccount(id)) {
            throw new ResourceNotFoundException("User account not found");
        }
        accountRepository.deleteById(id);
    }

    /**
     * Checks whether a user account exists in the database.
     *
     * @param userAccountId the id of the account.
     * @return true if the account exists, false otherwise.
     */
    private boolean existsUserAccount(Long userAccountId) {
        return accountRepository.existsById(userAccountId);
    }
}
