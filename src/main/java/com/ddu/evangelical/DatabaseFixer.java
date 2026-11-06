package com.ddu.evangelical;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Component
public class DatabaseFixer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {

            System.out.println("🛠️ FORCING DATABASE TABLE RECREATION...");

            // Drop the broken tables
            stmt.execute("DROP TABLE IF EXISTS posts CASCADE;");
            stmt.execute("DROP TABLE IF EXISTS photos CASCADE;");
            stmt.execute("DROP TABLE IF EXISTS pdfs CASCADE;");

            System.out.println("✅ TABLES DROPPED SUCCESSFULLY!");

        } catch (Exception e) {
            System.out.println("❌ Database fix failed: " + e.getMessage());
        }
    }
}