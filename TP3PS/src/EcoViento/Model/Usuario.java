package EcoViento.Model;

public abstract class Usuario {
 private final String username;
 private final Rol rol;

 protected Usuario(String username, Rol rol) {
     this.username = username;
     this.rol = rol;
 }

 public String getUsername() { return username; }
 public Rol getRol() { return rol; }
}
