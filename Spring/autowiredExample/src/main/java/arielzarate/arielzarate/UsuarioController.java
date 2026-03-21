package arielzarate.arielzarate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioController {

    /**
     *
     * Podemos usar autowired para inyectar dependencias
     * con el Autowired no es necesario usar el constructor
     * */

    @Autowired
    private UsuarioService usuarioService;



    /**
     * Podemos usar el constructor para inyectar dependencias
     **/
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }



    /**
     * La 3era opciones es usar autowired en el setter
     * con el Autowired no es necesario usar el constructor
     * */
    @Autowired
    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    /**
     * La cuatra opcion es usar  lombok con @AllArgsConstructor
     * y no es necesario usar el constructor
     * y no es necesario usar el Autowired
     * */

    // solo pongo @AllArgsConstructor en la clase
    // y declaro private final UsuarioService usuarioService;

}
