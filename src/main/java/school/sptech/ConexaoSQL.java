package school.sptech;

import com.mysql.cj.jdbc.MysqlDataSource;

public class ConexaoSQL {
    private MysqlDataSource dataSource;

    // Criando o DataSource para conexão
    public ConexaoSQL() {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(3306);
        dataSource.setDatabaseName("educadata");
        dataSource.setUser("Caramico");
        dataSource.setPassword("urubu100");
    }
}
