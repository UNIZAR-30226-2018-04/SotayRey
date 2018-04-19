<!-- Añadir articulo -->
<div id="anyadirArticulo" class="modal fade" role="dialog">
    <div class="modal-dialog">

        <!-- Modal content -->
        <div class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title w-100 font-weight-bold">Añadir artículo</h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body container">
                <div class="row">
                    <div class="col-sm">
                        <form action="/AnyadirArticuloTienda.do" method="post">
                            <div class="form-group">
                                <label for="tipoArticulo">Tipo item</label>
                                <select name="tipo" class="form-control">
                                    <option value="avatar">Avatar</option>
                                    <option value="baraja">Baraja</option>
                                    <option value="dorso">Dorso</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="descripcion">Descripción</label>
                                <input type="text" class="form-control" name="descripcion" id="descripcion"
                                       placeholder="Descripción del item">
                            </div>
                            <div class="form-group">
                                <label for="precio">Precio</label>
                                <input type="text" class="form-control" name="precio" id="precio"
                                       placeholder="Introduce el precio del ítem">
                            </div>
                            <div class="form-group">
                                <label for="email">Restriccion</label>
                                <select name="tipo" class="form-control">
                                    <option value="no_restriccion">Sin restriccion</option>
                                    <option value="bronce">Bronce</option>
                                    <option value="oro">Oro</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="passwd">RUTAS IMAGEN VISTA Y USABLE</label>
                                <input type="password" class="form-control" name="passwd" id="passwd"
                                       placeholder="Contraseña">
                            </div>
                            <button type="submit" class="btn btn-primary">Crear</button>
                        </form>
                    </div>
                    <div class="col-sm">
                        <a>Columna</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>