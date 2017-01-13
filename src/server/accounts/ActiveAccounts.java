package server.accounts;

import java.util.concurrent.ConcurrentHashMap;

public class ActiveAccounts {
    private static ActiveAccounts activeAccounts;
    /**
     * String - клиентский IP
     */
    private final ConcurrentHashMap<String, Account> accountsInUse;
    private final long timeoutMillis;

    private ActiveAccounts() {
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

    public void update() {
        accountsInUse.entrySet().removeIf(stringAccountEntry ->
                System.currentTimeMillis() - stringAccountEntry.getValue().getLastAccessTime() > timeoutMillis ||
                        !stringAccountEntry.getKey().equals(stringAccountEntry.getValue().getIP()));
    }
}
