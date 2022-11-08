package voltskiya.mob.system.base;

public class VoltskiyaMobDatabaseConfig {

    private String username = "${username}";
    private String password = "${password}";
    private String host = "${host}";
    private String port = "${port}";
    private String database = "MobSystem";

    public String getUrl() {
        return "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
