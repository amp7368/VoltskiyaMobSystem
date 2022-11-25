package voltskiya.mob.system.base;

public class VoltskiyaMobDatabaseConfig {

    public String username = "${username}";
    public String password = "${password}";
    public String host = "${host}";
    public String port = "${port}";
    public String database = "mob_system";

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
