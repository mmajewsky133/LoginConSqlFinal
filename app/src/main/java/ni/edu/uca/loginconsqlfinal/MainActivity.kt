package ni.edu.uca.loginconsqlfinal

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import java.sql.*

class MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
    }

    fun login(view: View) {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (username.isNotEmpty() && password.isNotEmpty()) {

            try {
                val loginTask = LoginTask()

                loginTask.execute(username, password)
            }
            catch (ex: Exception){
                Toast.makeText(this, "No", Toast.LENGTH_SHORT).show()}
        } else {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
        }
    }

    inner class LoginTask : AsyncTask<String, Void, Boolean>() {

        private lateinit var connection: Connection

        override fun doInBackground(vararg params: String?): Boolean {
            val username = params[0]
            val password = params[1]

            val connectionString = "jdbc:jtds:sqlserver://186.77.197.95:1433;databaseName=Autenticacion;user=sa;password=1234;"
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            try {
                connection = DriverManager.getConnection(connectionString)
                Toast.makeText(applicationContext, "Login yes", Toast.LENGTH_SHORT).show()
            } catch (ex: SQLException) {
                ex.printStackTrace()
           Toast.makeText(applicationContext, ex.message, Toast.LENGTH_SHORT).show()
                return false
            }



            val query = "SELECT * FROM users WHERE username=? AND password=?"
            val statement: PreparedStatement = connection.prepareStatement(query)
            statement.setString(1, username)
            statement.setString(2, password)

            val resultSet: ResultSet = statement.executeQuery()
            return resultSet.next()
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)

            if (result == true) {
                Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }

            connection.close()
        }
    }
}