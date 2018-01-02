package database;

import java.sql.*;

/**
 * Created by Alicja on 2017-12-27.
 */
public class Database {
    private static final String DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DB_USER = "JAVA_PROJEKT";
    private static final String DB_PASSWORD = "java";
    private static final String ODPOWIEDZ = "select tresc " + "from odpowiedzi where numer" + " = ? " + "and ABCD" + " = ? ";
    private Connection connection;

    /***
     * lazy initialization - instance of this class won't be created, until
     * the first time other class calls the getInstance method
     */
    private static Database instance = new Database();

    /***
     *  private constructor, cannot be used outside this class
     */
    private Database() {
    }

    /***
     * @return singleton - there is only one instance of this object
     */
    public static Database getInstance() {
        return instance;
    }


    public boolean open( String DB_USER) {

        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * close connection to db
     */
    public void close() {

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    private static int executeUpdate(Statement s, String sql) {
        try {
            return s.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
        }
        return -1;
    }

    private static Statement createStatement(Connection connection) {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Błąd createStatement! " + e.getMessage() + ": " + e.getErrorCode());
            System.exit(3);
        }
        return null;
    }

    public void create() {


        try {
            //CREATE TABLE PYTANIA(numer INT NOT NULL, tresc VARCHAR(50), poprawna_odpowiedz VARCHAR(50), PRIMARY KEY (numer) );
//CREATE TABLE odpowiedzi(id INT NOT NULL, numer INT NOT NULL ,tresc VARCHAR(50), PRIMARY KEY (numer,id) );
            //CREATE TABLE wyniki(id INT NOT NULL, id_studenta int not null, punkty int not null, PRIMARY KEY (id) );
            //CREATE TABLE studenci(id_studenta int not null, imie varchar(20), nazwisko varchar(20));
            Statement st = connection.createStatement();
            System.out.println("wykonuje zapytanie");
            st.executeUpdate("CREATE TABLE coskkk( id INT NOT NULL, tytul VARCHAR(50) NOT NULL, autor INT NOT NULL, PRIMARY KEY (id) )");
            //  String sql = "INSERT INTO ksiazki_ VALUES(2, 'Pan adeusz', 2)";
            //  String sql2 = "Select * from ksiazki_";
            //     st.executeUpdate(sql);
//INSERT INTO PYTANIA(NUMER, POPRAWNA_ODPOWIEDZ,TRESC)  VALUES (1,'Interfejs może mieć tylko jeden konstruktor.','Które z poniższych zdań są prawdziwe');
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public String readQuestion(int numer) {
        Statement stmt = null;
        ResultSet rs = null;

        try {

            stmt = connection.createStatement();
            rs = stmt.executeQuery("select tresc " +
                    "from pytania WHERE numer=" + numer);
            while (rs.next()) {
                String tytul = rs.getString("tresc");
                //.out.println(tytul);
                return tytul;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("niepowodzenie w trakcie wykonywania zapytania");
        }

        return "blad";
    }



    public String readAnswers(int numerPytania, String abcd) {
        PreparedStatement stmt = null;

        try {

            stmt = connection.prepareStatement(ODPOWIEDZ);
            stmt.setInt(1, numerPytania);
            stmt.setString(2, abcd);
            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                String tytul = rs.getString("tresc");
                return tytul;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("niepowodzenie w trakcie wykonywania zapytania");
        }

        return "blad";
    }

    //Create Table Studenci_Odpowiedzi(id_studenta int not null,numer_pytania int not null, odpowiedz Varchar(100)) ;
    public void insertwynik(int niu, int numerPytania, String odpowiedz) {
        String sql = "INSERT INTO " + "STUDENCI_ODPOWIEDZI(ID_STUDENTA,NUMER,odpowiedz)" + "VALUES" + "(?, ?, ?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, niu);
            st.setInt(2, numerPytania);
            st.setString(3, odpowiedz);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int ilePytan() {
        String table = "PYTANIA";
        String column = "NUMER";

        String sql = "SELECT MAX(" + column + ") AS max FROM " + table;
        try (Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(sql)) {
            int maxId = 0;
            if (result.next()) {
                maxId = result.getInt(1);
            }
            return maxId;
        } catch (SQLException e) {
            System.out.println("getID() error: " + e.getMessage());
            return -1;
        }
    }

    public int compareAnswersToTrue(int numerPytania, int idStudenta) { //"select tresc " + "from odpowiedzi where numer"+" = ? "+"and ABCD"+" = ? "
        String sql = "Select POPRAWNA_ODPOWIEDZ from PYTANIA where NUMER=" + " ?";
        String sqlUdzielona = "Select ODPOWIEDZ from STUDENCI_ODPOWIEDZI where NUMER=" + "?" + "and ID_STUDENTA=" + "?";
        try {
            PreparedStatement st1 = connection.prepareStatement(sql);
            PreparedStatement st2 = connection.prepareStatement(sqlUdzielona);
            st1.setInt(1, numerPytania);
            st2.setInt(1, numerPytania);
            st2.setInt(2, idStudenta);
            ResultSet rs1 = st1.executeQuery();
            ResultSet rs2 = st2.executeQuery();
            while (rs1.next() && rs2.next()) {
                String poprawna = rs1.getString("poprawna_odpowiedz");
                String udzielona = rs2.getString("odpowiedz");
                if (poprawna.equals(udzielona))
                    return 1;
                else
                    return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 100;
    }

    public void dodajWynik(int wynik, int id) {

        String sql = "INSERT INTO " + "WYNIKI(id_studenta,punkty)" + "VALUES" + "(?, ?)";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.setInt(2, wynik);

            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public String podajWynik(int id) {

        PreparedStatement stmt = null;
        String sql = "SELECT PUNKTY FROM WYNIKI WHERE ID_STUDENTA=" + "?";
        try {

            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                Integer wynik = rs.getInt("PUNKTY");
                return wynik.toString();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("niepowodzenie w trakcie wykonywania zapytania");
        }


        return "nic";
    }


}
