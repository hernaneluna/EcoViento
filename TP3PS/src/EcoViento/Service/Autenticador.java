package EcoViento.Service;

import EcoViento.Model.Admin;
import EcoViento.Model.Operador;
import EcoViento.Model.Usuario;

public class Autenticador {
    public Usuario login(String user, String pass){
        if ("1234".equals(pass)) {
            if ("adm".equalsIgnoreCase(user))      return new Admin("adm");
            if ("operador".equalsIgnoreCase(user)) return new Operador("operador");
        }
		return null;
    }
}
