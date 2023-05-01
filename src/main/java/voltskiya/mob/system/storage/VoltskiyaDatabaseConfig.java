package voltskiya.mob.system.storage;

public class VoltskiyaDatabaseConfig {

    private static VoltskiyaDatabaseConfig instance;
    protected String username = "${username}";
    protected String password = "${password}";
    protected String database = "mob_system";
    protected String port = "${port}";
    protected String host = "${host}";

    public VoltskiyaDatabaseConfig() {
        instance = this;
    }

    public static VoltskiyaDatabaseConfig get() {
        return instance;
    }

    public String getUrl() {
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;
    }

    public String getDatabase() {
        return this.database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
