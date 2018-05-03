# ------------ Compilacion y ejecucion --------------
javac -cp basedatos/exceptions/*.java && echo Exceptions compiladas && javac -cp ../lib/*:. basedatos/exceptions/*.java basedatos/modelo/*.java && echo VO compilados && javac -cp ../lib/*:. basedatos/exceptions/*.java basedatos/modelo/*.java  basedatos/BCrypt.java basedatos/dao/*.java && echo DAO compilados && javac -cp ../lib/*:. basedatos/exceptions/*.java basedatos/modelo/*.java basedatos/dao/*.java basedatos/BCrypt.java basedatos/InterfazDatos.java basedatos/Main.java && echo FIN COMPILACION && java -cp ../lib/*:. basedatos.Main

