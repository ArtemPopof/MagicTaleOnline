package server.accounts;

import java.util.concurrent.ConcurrentHashMap;

public class ActiveAccounts {
    private static ActiveAccounts activeAccounts;
    private final AccountsStorage accounts;
    /**
     * String - клиентский IP
     */
    private final ConcurrentHashMap<String, Account> accountsInUse;
    private final long timeoutMillis;

    private ActiveAccounts() {
        accounts = AccountsStorage.getInstance();
        accountsInUse = new ConcurrentHashMap<>();
        // клиент будет отключен после 5 минут бездействия
        timeoutMillis = 300_000;
    }

    public static ActiveAccounts getInstance() {
        if (activeAccounts == null) {
            activeAccounts = new ActiveAccounts();
        }

        return activeAccounts;
    }

    /**
     * очистка от неактивных последних 5 минут клиентов
     */
    public void filter() {
        accountsInUse.entrySet()
                .removeIf(stringAccountEntry ->
                        System.currentTimeMillis() - stringAccountEntry.getValue().getLastAccessTime() > timeoutMillis
                                || !stringAccountEntry.getKey().equals(stringAccountEntry.getValue().getIP()));
    }

    public void setEnable(String ip, String nickname) {
        accountsInUse.put(ip, accounts.getAccount(nickname));
    }

    public String[] getIPs() {
        return accountsInUse.values()
                .stream()
                .map(Account::getIP)
                .toArray(String[]::new);
    }
}
