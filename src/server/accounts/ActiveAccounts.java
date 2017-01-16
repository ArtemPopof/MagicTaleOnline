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
        int clientsCount = accountsInUse.size();
        accountsInUse.entrySet()
                .removeIf(stringAccountEntry ->
                        System.currentTimeMillis() - stringAccountEntry.getValue().getLastAccessTime() > timeoutMillis
                                || !stringAccountEntry.getKey().equals(stringAccountEntry.getValue().getIP()));
        clientsCount -= accountsInUse.size();
        if (clientsCount > 0) {
            System.out.println("-" + clientsCount + " clients");
        }
    }

    public void setEnable(String ip, String nickname) {
        System.out.println("+1 client");
        Account account = accounts.getAccount(nickname);
        account.setIP(ip);
        account.updateLastAccess();
        accountsInUse.put(ip, account);
        System.out.println("Now " + accountsInUse.size() + " clients");
    }

    public String[] getIPs() {
        return accountsInUse.values()
                .stream()
                .map(Account::getIP)
                .toArray(String[]::new);
    }

    public Account getAccount(String ip) {
        return accountsInUse.getOrDefault(ip, null);
    }

    public void tick() {
        accountsInUse.values().forEach(Account::tick);
//        System.out.println("Send status to " + accountsInUse.size() + " players");
    }
}
