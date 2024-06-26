package database;

import entity.*;
import util.PrintablePreparedStatement;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnectionHandler {
    // Use this version of the ORACLE_URL if you are running the code off of the server
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void rollbackConnection() {
        try  {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public User[] getUserInfo() {
        ArrayList<User> result = new ArrayList<User>();

        try {
            String query = "SELECT * FROM USERTABLE";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                User model = new User(rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getDate("Birthday"));
                result.add(model);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new User[result.size()]);
    }

    public Account[] getAccountInfo() {
        ArrayList<Account> result = new ArrayList<Account>();
        try {
            String query = "SELECT * FROM ACCOUNT";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Account model = new Account(rs.getInt("UserID"),
                        rs.getString("Password"),
                        rs.getString("Language"),
                        rs.getString("Email"));
                result.add(model);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new Account[result.size()]);
    }

    private void dropBranchTableIfExists() {
        try {
            String query = "select table_name from user_tables";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            try {ps.execute("DROP TABLE UserTable cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE Account cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE SavingData cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE Roles cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE UserTable cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE Weapons cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE Map cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE LockedArea cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE UnlockArea cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE Coordinate cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE LearnSkills_Stats cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE LearnSkills_Info cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE BossInfo cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE DungeonStats cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE DungeonInfo cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE Characters_Stats cascade constraints purge");}
            catch (SQLException e){ }
            try {ps.execute("DROP TABLE Characters_Info cascade constraints purge");}
            catch (SQLException e){ }
            ps.close();
            System.out.println("Drop finished");
        } catch (SQLException e) {
//            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void insertUserModel(User model) {
        try {
            String query = "INSERT INTO USERTABLE VALUES (?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, model.getEmail());
            ps.setString(2, model.getName());
            ps.setDate(3, model.getBirthday());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertRolesModel(Role role) {
        try {
            String query = "INSERT INTO ROLES VALUES (?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, role.getRoleName());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertWeapon(Weapons weapons) {
        try {
            String query = "INSERT INTO WEAPONS VALUES (?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, weapons.getWeaponID());
            ps.setInt(2, weapons.getwpDamage());
            ps.setInt(3, weapons.getPrice());
            ps.setString(4, weapons.getRname());
            ps.setString(5, weapons.getWname());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertMapModel(Map map) {
        try {
            String query = "INSERT INTO MAP VALUES (?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, map.getMapID());
            ps.setString(2, map.getMapName());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertCharacterInfoModel(CharacterInfo characterInfo) {
        try {
            String query = "INSERT INTO Characters_Info VALUES (?,?,?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, characterInfo.getCharacterName());
            ps.setInt(2, characterInfo.getLevel());
            ps.setInt(3, characterInfo.getMoney());
            ps.setString(4, characterInfo.getRoleName());
            ps.setString(5, characterInfo.getMapID());
            ps.setString(6, characterInfo.getCurrLoc());

            ps.executeUpdate();
            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void insertAccountModel(Account model) {
        try {
            String query = "INSERT INTO ACCOUNT VALUES (?,?,?,?)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, model.getUID());
            ps.setString(2, model.getPassword());
            ps.setString(3, model.getLanguage());
            ps.setString(4, model.getEmail());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void initializeRoles(){
        insertRolesModel(new Role("Warrior"));
        insertRolesModel(new Role("Assassin"));
        insertRolesModel(new Role("Mage"));
        insertRolesModel(new Role("Archer"));
        insertRolesModel(new Role("Berserker"));
    }

    public void initializeMaps(){
        insertMapModel(new Map("M01", "Town"));
        insertMapModel(new Map("M02", "Forest"));
        insertMapModel(new Map("M03", "Ocean"));
        insertMapModel(new Map("M04", "Desert"));
        insertMapModel(new Map("M05", "Highland"));
    }

    public void initializeUsers() {
        User UserModel1 = new User("Devin", "Devin@gmail.com", Date.valueOf("2000-1-18"));
        insertUserModel(UserModel1);
        Account AccountModel1 = new Account(100000001, "p","Chinese", UserModel1.getEmail());
        insertAccountModel(AccountModel1);
    }

    public void initializeWeapons() {
        insertWeapon(new Weapons(1000000001, 100, 150, "Warrior", "Hammer"));
        insertWeapon(new Weapons(1000000001, 1, 150, "Warrior", "stick"));
        insertWeapon(new Weapons(1000000002, 20, 200, "Assassin", "Knife"));
        insertWeapon(new Weapons(1000000002, 51, 250, "Mage", "Magic wand"));
        insertWeapon(new Weapons(1000000002, 20, 300, "Archer", "Bow"));
        insertWeapon(new Weapons(1000000002, 20, 400, "Berserker", "Sword"));
    }

    public void deleteCharacterInfo(String charName) {
        try {
            String query = "DELETE FROM CHARACTERS_INFO WHERE CNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, charName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " CHARACTERS_INFO " + charName + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateCharacterMoney(int newMoneyValue, String charName) {
        try {
            String query = "UPDATE CHARACTERS_INFO SET MONEY = ? WHERE CNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, newMoneyValue);
            ps.setString(2, charName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Character " + charName + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateCharacterLevel(int newLevel, String charName) {
        try {
            String query = "UPDATE CHARACTERS_INFO SET CHARLEVEL = ? WHERE CNAME = ?";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setInt(1, newLevel);
            ps.setString(2, charName);

            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                System.out.println(WARNING_TAG + " Character " + charName + " does not exist!");
            }

            connection.commit();

            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }



    public void databaseSetup() {
        dropBranchTableIfExists();

        try {
            String query = "CREATE TABLE UserTable (\n" +
                    "    Email VARCHAR(50) PRIMARY KEY,\n" +
                    "    Name VARCHAR(50) NOT NULL,\n" +
                    "    Birthday DATE\n" +
                    ")";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating UserTable Table");
        }

        try {
            String query = "CREATE TABLE Account(\n" +
                    "    UserID INT PRIMARY KEY,\n" +
                    "    Password VARCHAR(50) NOT NULL,\n" +
                    "    Language VARCHAR(50) NOT NULL,\n" +
                    "    Email VARCHAR(50) NOT NULL,\n" +
                    "    FOREIGN KEY (Email) REFERENCES UserTable(Email)\n" +
                    ")";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating Account Table");
        }

        try {
            String query = "CREATE TABLE SavingData(\n" +
                    "    DataID CHAR(10),\n" +
                    "    UserID INT,\n" +
                    "    CreatingDate DATE NOT NULL,\n" +
                    "    PRIMARY KEY (DataID, UserID),\n" +
                    "    FOREIGN KEY (UserID) REFERENCES Account(UserID)\n" +
                    ")";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating SavingData Table");
        }

        try {
            String query = "CREATE TABLE Roles(\n" +
                    "                      Rname VARCHAR(50) PRIMARY KEY)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating Roles Table");
        }

        try {
            String query = "CREATE TABLE Weapons(\n" +
                    "                        WeaponID INTEGER,\n" +
                    "                        wpDamage INTEGER,\n" +
                    "                        Price INTEGER,\n" +
                    "                        Rname VARCHAR(50),\n" +
                    "                        Wname VARCHAR(50),\n" +
                    "                        FOREIGN KEY(Rname) REFERENCES Roles(Rname) on DELETE CASCADE)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating Weapons Table");
        }

        try {
            String query = "CREATE TABLE Map(\n" +
                    "                    MapID CHAR(10) PRIMARY KEY,\n" +
                    "                    MapName VARCHAR(50) NOT NULL\n" +
                    ")";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating Map Table");
        }

        try {
            String query = "CREATE TABLE LockedArea(\n" +
                    "                           MapID CHAR(10),\n" +
                    "                           MapName VARCHAR(50),\n" +
                    "                           FoggyArea CHAR(10),\n" +
                    "                           FOREIGN KEY(MapID) REFERENCES Map(MapID))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating LockedArea Table");
        }

        try {
            String query = "CREATE TABLE UnlockArea(\n" +
                    "                           MapID CHAR(10),\n" +
                    "                           MapName VARCHAR(50),\n" +
                    "                           CheckPoint CHAR(10),\n" +
                    "                           FOREIGN KEY(MapID) REFERENCES Map(MapID))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating UnlockArea Table");
        }

        try {
            String query = "CREATE TABLE Coordinate(\n" +
                    "                           X_Coord CHAR(10),\n" +
                    "                           MapID CHAR(10),\n" +
                    "                           Y_Coord CHAR(10),\n" +
                    "                           PRIMARY KEY(X_Coord,Y_Coord, MapID),\n" +
                    "                           FOREIGN KEY(MapID) REFERENCES Map(MapID))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating Coordinate Table");
        }

        try {
            String query = "CREATE TABLE LearnSkills_Stats(\n" +
                    "                                  Sname VARCHAR(50),\n" +
                    "                                  SDamage INTEGER,\n" +
                    "                                  Requirement VARCHAR(50),\n" +
                    "                                  PRIMARY KEY(Sname))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating LearnSkills_Stats Table");
        }

        try {
            String query = "CREATE TABLE LearnSkills_Info(\n" +
                    "                                 skillID CHAR(10) PRIMARY KEY,\n" +
                    "                                 Sname VARCHAR(50),\n" +
                    "                                 IsLearned CHAR(1),\n" +
                    "                                 Rname VARCHAR(50),\n" +
                    "                                 FOREIGN KEY(Rname) REFERENCES Roles(Rname) on DELETE CASCADE)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating LearnSkills_Info Table");
        }

        try {
            String query = "CREATE TABLE BossInfo(\n" +
                    "                         Bname VARCHAR(50),\n" +
                    "                         Blevel INTEGER,\n" +
                    "                         BossDMG INTEGER,\n" +
                    "                         BossHP INTEGER,\n" +
                    "                         PRIMARY KEY(Bname))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating BossInfo Table");
        }

        try {
            String query = "CREATE TABLE DungeonStats(\n" +
                    "                             dungeonName VARCHAR(50) PRIMARY KEY,\n" +
                    "                             clearStatus CHAR(1))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating DungeonStats Table");
        }

        try {
            String query = "CREATE TABLE DungeonInfo(\n" +
                    "                            dungeonID CHAR(10) PRIMARY KEY,\n" +
                    "                            dungeonName VARCHAR(50),\n" +
                    "                            Item VARCHAR(50),\n" +
                    "                            Bname VARCHAR(50),\n" +
                    "                            BossID CHAR(10),\n" +
                    "                            MapID CHAR(10),\n" +
                    "                            FOREIGN KEY(MapID) REFERENCES Map(MapID))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating DungeonInfo Table");
        }

        try {
            String query = "CREATE TABLE Characters_Stats(\n" +
                    "                                 HP INTEGER,\n" +
                    "                                 Playtime INT,\n" +
                    "                                 charLevel INTEGER PRIMARY KEY)";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating Characters_Stats Table");
        }

        try {
            String query = "CREATE TABLE Characters_Info(\n" +
                    "                                Cname VARCHAR(50) PRIMARY KEY,\n" +
                    "                                charLevel INTEGER,\n" +
                    "                                Money INTEGER,\n" +
                    "                                Rname VARCHAR(50),\n" +
                    "                                MapID CHAR(10),\n" +
                    "                                currLoc CHAR(20),\n" +
                    "                                FOREIGN KEY(MapID) REFERENCES Map(MapID),\n" +
                    "                                FOREIGN KEY(Rname) REFERENCES Roles(Rname))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            System.out.println("Error in Creating Characters_Info Table");
        }
    }

    public DefaultTableModel groupBy() {
        String[] columns = {"Role Name", "Max Character Level"};
        DefaultTableModel table = new DefaultTableModel(columns,0);
        try {
            String query = "SELECT C.rname, max(CHARLEVEL) FROM CHARACTERS_INFO C, ROLES R WHERE C.RNAME = R.RNAME GROUP BY C.rname";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Object[] row = new Object[columns.length];
                row[0] = rs.getString("rname");
                row[1] = rs.getString("max(CHARLEVEL)");
                table.addRow(row);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return table;
    }

    public DefaultTableModel findAll() {
        String[] columns = {"Map ID", "Map Name"};
        DefaultTableModel table = new DefaultTableModel(columns,0);
        try {
            String query = "SELECT MAPID, MAPNAME FROM MAP M where not exists ((select CNAME from CHARACTERS_INFO C where C.money > 100) minus (select CNAME from CHARACTERS_INFO C1 where c1.MAPID=M.MAPID AND C1.money > 100))";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Object[] row = new Object[columns.length];
                row[0] = rs.getString("MAPID");
                row[1] = rs.getString("MAPNAME");
                table.addRow(row);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return table;
    }

    public DefaultTableModel having() {
        String[] columns = {"Role Name", "Max Weapon Damage"};
        DefaultTableModel table = new DefaultTableModel(columns,0);
        try {
            String query = "select R.rname, max(S.WPDAMAGE) from ROLES R, WEAPONS S where R.RNAME = S.RNAME group by R.rname having max(S.WPDAMAGE)> 50";
            PrintablePreparedStatement ps = new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Object[] row = new Object[columns.length];
                row[0] = rs.getString("RNAME");
                row[1] = rs.getString("MAX(S.WPDAMAGE)");
                table.addRow(row);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return table;
    }

    public String[] getAffordableWeapons(String cname) {
        ArrayList<String> weaponNames = new ArrayList<>();

        try {
            //retrieve the character's current money
            String retrieveMoneyQuery =
                    "SELECT Money FROM Characters_Info WHERE Cname = ?";
            PrintablePreparedStatement moneyPs =
                    new PrintablePreparedStatement(connection.prepareStatement(retrieveMoneyQuery),
                            retrieveMoneyQuery,
                            false);
            moneyPs.setString(1, cname);
            ResultSet moneyRs = moneyPs.executeQuery();
            int money = 0;
            if (moneyRs.next()) {
                money = moneyRs.getInt("Money");
            }
            moneyRs.close();
            moneyPs.close();

            // Find affordable weapons
            String affordableWeaponsQuery =
                    "SELECT Wname FROM Weapons WHERE Price <= ?";
            PrintablePreparedStatement weaponsPs =
                    new PrintablePreparedStatement(connection.prepareStatement(affordableWeaponsQuery),
                            affordableWeaponsQuery,
                            false);
            weaponsPs.setInt(1, money);
            ResultSet weaponsRs = weaponsPs.executeQuery();

            while (weaponsRs.next()) {
                weaponNames.add(weaponsRs.getString("Wname"));
            }
            weaponsRs.close();
            weaponsPs.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return weaponNames.toArray(new String[0]);
    }

    public DefaultTableModel getProjectionFromSQL(String[] columns){
        DefaultTableModel projectionTable = new DefaultTableModel(columns,0);
        String allForQuery = "SELECT ";
        int countLength = 1;
        for(String i: columns){
            if(countLength == columns.length){
                allForQuery = allForQuery + i;
            } else {
                allForQuery = allForQuery + i + ", ";
                countLength++;
            }
        }
        allForQuery = allForQuery + " FROM Characters_Info";

        try {
            String query = allForQuery;
            PrintablePreparedStatement ps =
                    new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Object[] row = new Object[columns.length];
                int count = 0;
                for(String i: columns){
                    if(i == "Cname"){
                        row[count] = rs.getString("Cname");
                        count++;
                    }
                    if(i == "charLevel"){
                        row[count] = rs.getString("charLevel");
                        count++;
                    }
                    if(i == "Money"){
                        row[count] = rs.getString("Money");
                        count++;
                    }
                    if(i == "Rname"){
                        row[count] = rs.getString("Rname");
                        count++;
                    }
                    if(i == "MapID"){
                        row[count] = rs.getString("MapID");
                        count++;
                    }
                    if(i == "currLoc"){
                        row[count] = rs.getString("currLoc");
                        count++;
                    }
                }
                projectionTable.addRow(row);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
        return projectionTable;
    }

    public DefaultTableModel getCharacterWeaponByRole(String cname) {
        String[] columns = {"Character Name", "Level", "Role", "Weapon Name", "Weapon Damage", "Weapon Price"};
        DefaultTableModel table = new DefaultTableModel(columns,0);

        try {
            String query = "SELECT C.Cname, C.charLevel, C.Rname, W.Wname, W.wpDamage, W.Price " +
                    "FROM Characters_Info C LEFT JOIN Weapons W ON C.Rname = W.Rname " +
                    "WHERE C.Cname = ?";

            PrintablePreparedStatement ps =
                    new PrintablePreparedStatement(connection.prepareStatement(query), query, false);
            ps.setString(1, cname);

            ResultSet rs = ps.executeQuery();

            if(!rs.isBeforeFirst()) {
                // If ResultSet is empty (no weapons found for character's role)
                Object[] row = new Object[] {cname, "-", "-", "No weapon for this role", "-", "-"};
                table.addRow(row);
            }else{
                while(rs.next()) {
                    Object[] row = new Object[columns.length];
                    row[0] = rs.getString("Cname");
                    row[1] = rs.getInt("charLevel");
                    row[2] = rs.getString("Rname");
                    row[3] = rs.getString("Wname");
                    row[4] = rs.getInt("wpDamage");
                    row[5] = rs.getInt("Price");
                    table.addRow(row);
                }
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return table;
    }

    public DefaultTableModel getRolesWithMinAverageWeaponDamage() {
        String[] columns = {"Role Name", "Average Damage"};
        DefaultTableModel table = new DefaultTableModel(columns, 0);

        try {
            String query =
                    "SELECT R.RNAME, AVG(W.WPDAMAGE) AS AVG_DAMAGE "+
                            "FROM ROLES R "+
                            "JOIN WEAPONS W ON R.RNAME = W.RNAME "+
                            "GROUP BY R.RNAME "+
                            "HAVING AVG(W.WPDAMAGE) = ("+
                            "    SELECT MIN(AVG_DAMAGE) FROM ("+
                            "        SELECT AVG(WPDAMAGE) AS AVG_DAMAGE "+
                            "        FROM WEAPONS "+
                            "        GROUP BY RNAME"+
                            "    )"+
                            ")";

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[columns.length];
                row[0] = rs.getString("RNAME");
                row[1] = rs.getInt("AVG_DAMAGE");
                table.addRow(row);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return table;
    }

}
