/*
 * Autor: Sergio Izquierdo Barranco
 * Fecha: 22-03-18
 * Fichero: Objeto Artículo
 */

public class ArticuloVO {
    private String nombre;
    private int precio;
    private String descripcion;
    private String rutaImagen;
    private char tipo;
    //private Liga requiere;

    public ArticuloVO(String nombre, int precio, String descripcion, String rutaImagen, char tipo) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.tipo = tipo;
    }

//    public ArticuloVO(String nombre, int precio, String descripcion, String rutaImagen, char tipo, Liga requiere) {
//        this.nombre = nombre;
//        this.precio = precio;
//        this.descripcion = descripcion;
//        this.rutaImagen = rutaImagen;
//        this.tipo = tipo;
//        this.requiere = requiere;
//    }

    public String getNombre() {
        return nombre;
    }

    public void setNmbre(String nmbre) {
        this.nombre = nmbre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

//    public Liga getRequiere() {
//        return requiere;
//    }
//
//    public void setRequiere(Liga requiere) {
//        this.requiere = requiere;
//    }
}
