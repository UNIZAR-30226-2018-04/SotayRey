
/*
 * Autor: Sergio Izquierdo Barranco
 * Fecha: 22-03-18
 * Fichero: Objeto ArtículoUsuario
 */

package basedatos.modelo;

public class ArticuloUsuarioVO extends ArticuloVO {
    private boolean disponible;
    private boolean favorito;
    private boolean comprado;
    private String username;

    public ArticuloUsuarioVO(String nombre, int precio, String descripcion, String rutaImagen, char tipo, boolean disponible, boolean favorito, boolean comprado, String username) {
        super(nombre, precio, descripcion, rutaImagen, tipo);
        this.disponible = disponible;
        this.comprado = comprado;
        this.username = username;
        this.favorito  = favorito;
    }



//    public ArticuloUsuarioVO(String nombre, int precio, String descripcion, String rutaImagen, char tipo, Liga requiere, boolean disponible, boolean favorito, boolean comprado, String username) {
//        super(nombre, precio, descripcion, rutaImagen, tipo, requiere);
//        this.disponible = disponible;
//        this.comprado = comprado;
//        this.username = username;
//        this.favorito = favorito;
//    }


    public boolean isFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
