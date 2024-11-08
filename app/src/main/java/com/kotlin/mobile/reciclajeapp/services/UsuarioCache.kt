import android.content.Context
import com.google.gson.Gson
import com.kotlin.mobile.reciclajeapp.model.Usuario

object UsuarioCache {

    fun obtenerUsuarioCache(context: Context): Usuario? {
        val sharedPreferences = context.getSharedPreferences("mi_app_prefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val usuarioJson = sharedPreferences.getString("usuario", null)
        return if (usuarioJson != null) {
            gson.fromJson(usuarioJson, Usuario::class.java)
        } else {
            null // o puedes devolver un nuevo objeto Usuario por defecto
        }
    }

    fun borrarUsuarioCache(context: Context) {
        val sharedPreferences = context.getSharedPreferences("mi_app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("usuario")
        editor.apply() // Guarda los cambios de manera asincrónica
    }
}
