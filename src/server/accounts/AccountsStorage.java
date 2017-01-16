package server.accounts;

import java.util.concurrent.ConcurrentHashMap;

/**
 * хранилище учетных записей игроков с всего лишь 1м методом: получение аккаунта, если аккаунта не существует, он будет
 * создан с данным ником
 */
public class AccountsStorage {
    private static AccountsStorage storage;
    /**
     * String key - nickname of registered account
     */
    private final ConcurrentHashMap<String, Account> registeredAccounts;

    private AccountsStorage() {
        registeredAccounts = new ConcurrentHashMap<>();
    }

    public static AccountsStorage getInstance() {
        if (storage == null) {
            storage = new AccountsStorage();
        }

        return storage;
    }

    public Account getAccount(String nickname) {
        Account account = registeredAccounts.get(nickname);
        if (account == null) {
            account = new Account(nickname);
            System.out.println("New account created: " + nickname);
        }
        registeredAccounts.putIfAbsent(nickname, account);
        return account;
    }
}
