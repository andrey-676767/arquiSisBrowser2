package com.browser.controller;

import com.browser.model.Categoria;
import com.browser.model.Descarga;
import com.browser.model.Marcador;
import com.browser.model.Tab;
import com.browser.services.DatabaseManager;
import com.browser.services.ExternalValidatorService;
import com.browser.structures.ArbolBinario;
import com.browser.structures.ListaDoble;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();

        Marcador[] marcadores = new Marcador[10];
        for (int i = 0; i < marcadores.length; i++) {
            marcadores[i] = new Marcador();
        }

        boolean salir = false;

        ListaDoble<Tab> tabs = new ListaDoble<>();
        ListaDoble<ListaDoble<Tab>> gTabs = new ListaDoble<>();
        gTabs.insertar(tabs);

        ListaDoble<Tab> grupoActual = tabs;

        Tab tab = new Tab();
        tabs.insertar(tab);
        Scanner s = new Scanner(System.in);

        Marcador ejemplo = new Marcador("google.com", "Google", "Por defecto");
        Categoria porDefecto = new Categoria("P", ejemplo);

        ArbolBinario<Categoria> categorias = new ArbolBinario<>(porDefecto);

        Tab actual = tab;

        ListaDoble<Descarga> descargas = new ListaDoble<>();

        db.cargarDatosEnArbol(categorias);

        while (!salir){
            //"Interfaz"
            System.out.println("Grupo " + gTabs.indiceDe(grupoActual));
            grupoActual.mostrarLista();
            System.out.println("\nurl:" + grupoActual.obtener(grupoActual.indiceDe(actual)).getUrl() + "\n<- (4) - (5) -> Fav: "
                    + Arrays.toString(marcadores) + "\n");
            System.out.println("""
                    
                    Menu:
                    1. Buscar en la pestana.
                    2. Ver el menu de marcadores.
                    3. Salir.
                    
                    4. Ir hacia atras en una pestana.
                    5. Ir hacia adelante en una pestana.
                    6. Administrar sus pestanas.
                    7. Gestionar grupos de pestanas.
                    8. Gestionar descargas.
                    """);
            int eleccion = 3;//El valor que se le asigne aca no importa
            //Manejo de errores
            do {
                try{
                    eleccion = s.nextInt();
                    break;
                }catch (InputMismatchException e){
                    System.out.println("Caracter no valido, seleccione un numero de 1 a 6.");
                    s.nextLine();
                }
            }while(true);
            s.nextLine();

            switch (eleccion){
                case 1:
                    System.out.println("URL: ");
                    String url;

                    do {
                        try{
                            url = s.nextLine();
                            break;
                        }catch (InputMismatchException e){
                            System.out.println("Escriba un enlace valido.");
                            s.nextLine();
                        }
                    }while(true);

                    ExternalValidatorService apiReal = new ExternalValidatorService();
                    System.out.println("-> [RED] Consultando ubicación del servidor en tiempo real...");

                    String infoJson = apiReal.obtenerInfoServidor(url);

                    // Mostramos la respuesta "cruda" de la API para que el profesor vea que es real
                    System.out.println("-> [RESPUESTA API]: " + infoJson);

                    if (!infoJson.contains("fail")) {
                        System.out.println("-> [OK] Conexión establecida. Navegando...");
                        grupoActual.obtener(grupoActual.indiceDe(actual)).setUrl(url);
                    } else {
                        System.out.println("-> [ERROR] No se pudo verificar la procedencia del sitio.");
                    }
                    
                    break;
                case 2:
                    boolean guardar = false;
                    while (!guardar){
                        System.out.println("    Menu de marcadores");
                        if (!Objects.equals(marcadores[0].getUrl(), "")){
                            System.out.println(Arrays.toString(marcadores));
                        }else System.out.println("No hay marcadores en favoritos");

                        System.out.println("""
                                
                                Opciones
                                
                                1. Agregar un nuevo marcador.
                                2. Borrar un marcador.
                                
                                3. Agregar marcador a favoritos.
                                4. Borrar marcador de favoritos.
                                5. Pasar marcador a favoritos.
                                
                                6. Editar atrubutos de un marcador.
                                7. Visitar un marcador.
                                8. Ver marcadores guardados (no favoritos).
                                
                                9. Quitar marcador de favoritos.
                                
                                0. Volver.
                                """);

                        do {
                            try{
                                eleccion = s.nextInt();
                                break;
                            }catch (InputMismatchException e){
                                System.out.println("Caracter no valido, seleccione un numero de 0 a 4.");
                                s.nextLine();
                            }
                        }while(true);

                        s.nextLine();
                        switch (eleccion) {
                            case 0:
                                guardar = true;
                                break;

                            case 1:
                                System.out.println("Titulo: ");
                                String titule = s.nextLine();
                                System.out.println("URL: ");
                                String urlMarc = s.nextLine();
                                System.out.println("com.browser.model.Categoria: ");
                                String categoria = s.nextLine();

                                Marcador m = new Marcador(urlMarc, titule, categoria);
                                Categoria aux = new Categoria(categoria.substring(0, 1).toUpperCase(), m);

                                db.guardarMarcador(titule, urlMarc, categoria);

                                if (!categorias.buscar(aux)) {
                                    categorias.insertarOrdenado(aux);
                                    break;
                                }
                                aux = categorias.obtener(aux);
                                aux.contenido.insertarAlFinal(m);


                                break;

                            case 2:
                                System.out.println("Mostrando marcadores \n(agrupados por categoria)");
                                categorias.recorrerInOrden(categorias.raiz);
                                System.out.println("\n");

                                System.out.println("Digite la categoria del marcador a borrar:");
                                String cat = s.nextLine();
                                aux = new Categoria(cat.substring(0, 1).toUpperCase(), null);
                                System.out.println("Digite el nombre del marcador a borrar:");
                                String nombre = s.nextLine();

                                if (!categorias.buscar(aux)) {
                                    System.out.println("com.browser.model.Categoria no encontrada (no existe)");
                                    break;
                                }

                                aux = categorias.obtener(aux);
                                if (aux == null) {
                                    System.out.println("com.browser.model.Marcador especificado no existe");
                                    break;
                                }
                                aux.borrarMarcador(nombre);
                                if (aux.contenido.size() == 0) {
                                    categorias.eliminarOrdenadoA(aux);
                                }

                                break;


                            case 3:
                                if (Objects.equals(marcadores[marcadores.length - 1].getUrl(), "")) {
                                    System.out.println("Titulo: ");
                                    String title = s.nextLine();
                                    System.out.println("URL: ");
                                    String urlM = s.nextLine();
                                    System.out.println("com.browser.model.Categoria: ");
                                    String category = s.nextLine();

                                    Marcador auxM;//No importa lo que se le asigne al apuntador
                                    int i;
                                    for (i = 0; i < marcadores.length; i++) {
                                        auxM = marcadores[i];
                                        if (auxM.getUrl().isBlank()) break;
                                    }
                                    marcadores[i] = new Marcador(urlM, title, category);
                                    break;
                                }
                                System.out.println("Titulo: ");
                                String title = s.nextLine();
                                System.out.println("URL: ");
                                String urlM = s.nextLine();
                                System.out.println("com.browser.model.Categoria: ");
                                String category = s.nextLine();

                                marcadores = redimensionarLista(marcadores, 1);
                                marcadores[marcadores.length - 1] = new Marcador(urlM, title, category);
                                break;

                            case 4:
                                System.out.println("Seleccione la posicion del marcador a borrar");
                                int i = 0;
                                listadoMarcadores(marcadores, i);

                                do {
                                    try {
                                        eleccion = s.nextInt();
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (marcadores.length - 1));
                                        s.nextLine();
                                    }
                                } while (true);
                                if (eleccion < 0 || eleccion >= marcadores.length) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (marcadores.length - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 0 || eleccion >= marcadores.length);
                                }
                                s.nextLine();

                                if (eleccion <= 9) {
                                    marcadores[eleccion] = new Marcador();
                                    break;
                                }
                                marcadores[eleccion] = null;
                                marcadores = redimensionarLista(marcadores, -1);
                                break;

                            case 5:
                                System.out.println("Mostrando marcadores \n(agrupados por categoria)");
                                categorias.recorrerInOrden(categorias.raiz);
                                System.out.println("\n");

                                System.out.println("Digite la categoria del marcador a pasar:");
                                cat = s.nextLine();
                                aux = new Categoria(cat.substring(0, 1).toUpperCase(), null);
                                System.out.println("Digite el nombre del marcador a pasar:");
                                nombre = s.nextLine();

                                if (!categorias.buscar(aux)) {
                                    System.out.println("com.browser.model.Categoria no encontrada (no existe)");
                                    break;
                                }

                                aux = categorias.obtener(aux);
                                if (aux == null) {
                                    System.out.println("com.browser.model.Marcador especificado no existe");
                                    break;
                                }

                                if (Objects.equals(marcadores[marcadores.length - 1].getUrl(), "")) {
                                    Marcador auxM;//No importa lo que se le asigne al apuntador

                                    for (i = 0; i < marcadores.length; i++) {
                                        auxM = marcadores[i];
                                        if (auxM.getUrl().isBlank()) break;
                                    }
                                    marcadores[i] = aux.pop(nombre);
                                    break;
                                }

                                marcadores = redimensionarLista(marcadores, 1);
                                marcadores[marcadores.length - 1] = aux.pop(nombre);
                                break;

                            case 6:
                                Marcador nuevo = null;

                                System.out.println("Desea editar de favoritos(1) o de otros(2)");
                                do {
                                    try {
                                        eleccion = s.nextInt();
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out.println("Caracter no valido, seleccione un numero de 1 a 2");
                                        s.nextLine();
                                    }
                                } while (true);
                                if (eleccion < 1 || eleccion > 2) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 1 y 2");
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 1 || eleccion > 2);
                                }


                                if (eleccion == 1) {
                                    System.out.println("Seleccione la posicion del marcador a editar");
                                    i = 0;
                                    listadoMarcadores(marcadores, i);

                                    do {
                                        try {
                                            eleccion = s.nextInt();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Caracter no valido, seleccione un numero de 0 a " + (marcadores.length - 1));
                                            s.nextLine();
                                        }
                                    } while (true);
                                    if (eleccion < 0 || eleccion >= marcadores.length) {
                                        do {
                                            System.out.println("Seleccione un indice valido");
                                            do {
                                                try {
                                                    eleccion = s.nextInt();
                                                    break;
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Indice no valido, seleccione un numero entre 0 y " + (marcadores.length - 1));
                                                    s.nextLine();
                                                }
                                            } while (true);
                                        } while (eleccion < 0 || eleccion >= marcadores.length);
                                    }
                                    s.nextLine();

                                    nuevo = marcadores[eleccion];
                                } else {
                                    System.out.println("Mostrando otros marcadores guardados\n(agrupados por categoria)");
                                    categorias.recorrerInOrden(categorias.raiz);
                                    System.out.println("\n");
                                    s.nextLine();

                                    System.out.println("Digite la categoria del marcador a editar:");
                                    cat = s.nextLine();
                                    aux = new Categoria(cat.substring(0, 1).toUpperCase(), null);
                                    System.out.println("Digite el nombre del marcador a editar:");
                                    nombre = s.nextLine();

                                    if (!categorias.buscar(aux)) {
                                        System.out.println("com.browser.model.Marcador no encontrado (no existe)");
                                        break;
                                    }

                                    aux = categorias.obtener(aux);
                                    nuevo = aux.obtener(nombre);
                                }


                                System.out.println("""
                                        Elija el atributo a editar:
                                        1. Titulo.
                                        2. URL.
                                        3. com.browser.model.Categoria.
                                        """);
                                int opcion;
                                do {
                                    try {
                                        opcion = s.nextInt();
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out.println("Caracter no valido, seleccione un numero de 1 a 3.");
                                        s.nextLine();
                                    }
                                } while (true);

                                s.nextLine();

                                switch (opcion) {
                                    case 1:
                                        System.out.println("Nuevo titulo: ");
                                        nuevo.setTitulo(s.nextLine());
                                        break;

                                    case 2:
                                        System.out.println("Nueva URL: ");
                                        nuevo.setUrl(s.nextLine());
                                        break;

                                    case 3:
                                        System.out.println("Nueva categoria: ");
                                        nuevo.setCategoria(s.nextLine());
                                        break;

                                    default:
                                        System.out.println("Caracter no valido, seleccione un numero entre 1 y 3.");
                                        break;
                                }

                                break;


                            case 7:
                                System.out.println("Desea visitar de favoritos(1) o de otros(2)");
                                do {
                                    try {
                                        eleccion = s.nextInt();
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out.println("Caracter no valido, seleccione un numero de 1 a 2");
                                        s.nextLine();
                                    }
                                } while (true);
                                if (eleccion < 1 || eleccion > 2) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 1 y 2");
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 1 || eleccion > 2);
                                }
                                s.nextLine();


                                if (eleccion == 1) {
                                    System.out.println("Seleccione la posicion del marcador a visitar");
                                    i = 0;
                                    listadoMarcadores(marcadores, i);

                                    do {
                                        try {
                                            eleccion = s.nextInt();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println("Caracter no valido, seleccione un numero de 0 a " + (marcadores.length - 1));
                                            s.nextLine();
                                        }
                                    } while (true);
                                    if (eleccion < 0 || eleccion >= marcadores.length) {
                                        do {
                                            System.out.println("Seleccione un indice valido");
                                            do {
                                                try {
                                                    eleccion = s.nextInt();
                                                    break;
                                                } catch (InputMismatchException e) {
                                                    System.out.println("Indice no valido, seleccione un numero entre 0 y " + (marcadores.length - 1));
                                                    s.nextLine();
                                                }
                                            } while (true);
                                        } while (eleccion < 0 || eleccion >= marcadores.length);
                                    }
                                    s.nextLine();

                                    nuevo = marcadores[eleccion];
                                } else {
                                    System.out.println("Mostrando otros marcadores guardados\n(agrupados por categoria)");
                                    categorias.recorrerInOrden(categorias.raiz);
                                    System.out.println("\n");
                                    s.nextLine();

                                    System.out.println("Digite la categoria del marcador a visitar:");
                                    cat = s.nextLine();
                                    aux = new Categoria(cat.substring(0, 1).toUpperCase(), null);
                                    System.out.println("Digite el nombre del marcador a visitar:");
                                    nombre = s.nextLine();

                                    if (!categorias.buscar(aux)) {
                                        System.out.println("com.browser.model.Marcador no encontrado (no existe)");
                                        break;
                                    }

                                    aux = categorias.obtener(aux);
                                    nuevo = aux.obtener(nombre);
                                }


                                grupoActual.obtener(grupoActual.indiceDe(actual)).setUrl(nuevo.getUrl());
                                guardar = true;
                                break;

                            case 8:
                                System.out.println("Mostrando otros marcadores guardados\n(agrupados por categoria)");
                                categorias.recorrerInOrden(categorias.raiz);
                                System.out.println("\n");


                                break;

                            case 9:
                                System.out.println("Seleccione la posicion del marcador a borrar");
                                i = 0;
                                listadoMarcadores(marcadores, i);

                                do {
                                    try {
                                        eleccion = s.nextInt();
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (marcadores.length - 1));
                                        s.nextLine();
                                    }
                                } while (true);
                                if (eleccion < 0 || eleccion >= marcadores.length) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (marcadores.length - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 0 || eleccion >= marcadores.length);
                                }
                                s.nextLine();

                                Marcador transporte = marcadores[eleccion];

                                if (eleccion <= 9) {
                                    marcadores[eleccion] = new Marcador();
                                } else {
                                    marcadores[eleccion] = null;
                                    marcadores = redimensionarLista(marcadores, -1);
                                }

                                aux = new Categoria(transporte.getCategoria().substring(0, 1).toUpperCase(), transporte);

                                if (!categorias.buscar(aux)) {
                                    categorias.insertarOrdenado(aux);
                                    break;
                                }
                                aux = categorias.obtener(aux);
                                aux.contenido.insertarAlFinal(transporte);
                                break;

                            default:
                                System.out.println("Caracter no valido, seleccione un numero entre 0 y 9.");
                                break;
                        }
                    }
                    break;

                case 3:
                    salir = true;
                    break;
                case 4:
                    System.out.println("Yendo hacia atras");
                    grupoActual.obtener(grupoActual.indiceDe(actual)).atras();
                    break;
                case 5:
                    System.out.println("Yendo hacia adelante");
                    grupoActual.obtener(grupoActual.indiceDe(actual)).adelante();
                    break;
                case 6:
                    guardar = false;
                    while (!guardar){
                        System.out.println("         Opciones con las pestañas\n");
                        System.out.println("""
                                1. Abrir una nueva pestana.
                                2. Cerrar una pestana.
                                3. Agregar la url de una pestana a marcadores.
                                4. Cambiar de pestana.
                                0. Volver.
                                """);

                        do {
                            try{
                                eleccion = s.nextInt();
                                break;
                            }catch (InputMismatchException e){
                                System.out.println("Caracter no valido, seleccione un numero de 0 a 4");
                                s.nextLine();
                            }
                        }while(true);

                        s.nextLine();

                        switch (eleccion){
                            case 0:
                                guardar = true;
                                break;

                            case 1:
                                Tab pestanaNueva = new Tab();
                                grupoActual.insertarAlFinal(pestanaNueva);
                                actual = pestanaNueva;
                                guardar = true;
                                break;

                            case 2:
                                grupoActual.mostrarLista();
                                System.out.println("\nDigite la posicion de la pestana que desea borrar: ");

                                do {
                                    try{
                                        eleccion = s.nextInt();
                                        break;
                                    }catch (InputMismatchException e){
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (grupoActual.size() - 1));
                                        s.nextLine();
                                    }
                                }while(true);
                                if (eleccion < 0 || eleccion >= grupoActual.size()) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (grupoActual.size() - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 0 || eleccion >= grupoActual.size());
                                }
                                s.nextLine();

                                grupoActual.remover(eleccion);
                                if (grupoActual.size() == 0){
                                    salir = true;
                                } else {
                                    actual = grupoActual.obtenerUltimo();
                                }
                                break;

                            case 3:
                                grupoActual.mostrarLista();
                                System.out.println("\nDigite la posicion de la pestana cuya url desea marcar: ");

                                do {
                                    try{
                                        eleccion = s.nextInt();
                                        break;
                                    }catch (InputMismatchException e){
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (grupoActual.size() - 1));
                                        s.nextLine();
                                    }
                                }while(true);
                                if (eleccion < 0 || eleccion >= grupoActual.size()) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (grupoActual.size() - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 0 || eleccion >= grupoActual.size());
                                }
                                s.nextLine();

                                if (Objects.equals(marcadores[marcadores.length - 1].getUrl(), "")){
                                    System.out.println("Titulo: ");
                                    String title = s.nextLine();
                                    String urlM = grupoActual.obtener(eleccion).getUrl();
                                    System.out.println("URL: " + urlM);
                                    System.out.println("com.browser.model.Categoria: ");
                                    String category = s.nextLine();

                                    Marcador aux;//No importa lo que se le asigne al apuntador
                                    int i;
                                    for (i = 0; i < marcadores.length; i++) {
                                        aux = marcadores[i];
                                        if (aux.getUrl().isBlank()) break;
                                    }
                                    marcadores[i] = new Marcador(urlM, title, category);
                                    break;
                                }
                                System.out.println("Titulo: ");
                                String title = s.nextLine();
                                String urlM = grupoActual.obtener(eleccion).getUrl();
                                System.out.println("URL: " + urlM);
                                System.out.println("com.browser.model.Categoria: ");
                                String category = s.nextLine();

                                marcadores = redimensionarLista(marcadores, 1);
                                marcadores[marcadores.length - 1] = new Marcador(urlM, title, category);
                                break;

                            case 4:
                                grupoActual.mostrarLista();
                                System.out.println("\nDigite el numero de la pestana que quiere ingresar: ");
                                int index;

                                do {
                                    try{
                                        index = s.nextInt();
                                        break;
                                    }catch (InputMismatchException e){
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (grupoActual.size() - 1));
                                        s.nextLine();
                                    }
                                }while(true);
                                if (index < 0 || index >= grupoActual.size()) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                index = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (grupoActual.size() - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (index < 0 || index >= grupoActual.size());
                                }

                                actual = grupoActual.obtener(index);
                                s.nextLine();
                                guardar = true;

                                break;

                            default:
                                System.out.println("Numero no valido, seleccione un numero entre 0 y 4");
                                break;
                        }
                    }
                    break;

                case 7:
                    guardar = false;

                    while (!guardar){
                        System.out.println("         Mostrando grupo(s) de pestanas\n");
                        gTabs.mostrarLista();
                        System.out.println("\n");

                        System.out.println("""
                                1. Crear un nuevo grupo.
                                2. Eliminar un grupo.
                                3. Cambiar de grupo.
                                4. Cambiar pestana de grupo.
                                5. Disolver un grupo.
                                
                                0. Volver.
                                """);

                        do {
                            try{
                                eleccion = s.nextInt();
                                break;
                            }catch (InputMismatchException e){
                                System.out.println("Caracter no valido, seleccione un numero de 0 a 5");
                                s.nextLine();
                            }
                        }while(true);

                        s.nextLine();

                        switch (eleccion){

                            case 0:
                                guardar = true;
                                break;

                            case 1:
                                System.out.println("Creando el grupo " + gTabs.size() + "...");
                                ListaDoble<Tab> nuevoGrupo = new ListaDoble<>();
                                Tab newTab = new Tab();
                                nuevoGrupo.insertar(newTab);
                                gTabs.insertar(nuevoGrupo);

                                grupoActual = nuevoGrupo;
                                actual = newTab;

                                guardar = true;
                                break;

                            case 2:
                                gTabs.mostrarLista();
                                System.out.println("Digite la posicion de grupo que desea borrar: ");

                                do {
                                    try{
                                        eleccion = s.nextInt();
                                        break;
                                    }catch (InputMismatchException e){
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (gTabs.size() - 1));
                                        s.nextLine();
                                    }
                                }while(true);
                                if (eleccion < 0 || eleccion >= gTabs.size()) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (gTabs.size() - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 0 || eleccion >= gTabs.size());
                                }
                                s.nextLine();

                                gTabs.remover(eleccion);
                                if (gTabs.size() == 0){
                                    salir = true;
                                    guardar = true;
                                } else {
                                    grupoActual = gTabs.obtener(eleccion - 1);
                                    actual = grupoActual.obtenerUltimo();
                                }
                                break;

                            case 3:
                                gTabs.mostrarLista();
                                System.out.println("Digite la posicion de grupo que desea ingresar: ");

                                do {
                                    try{
                                        eleccion = s.nextInt();
                                        break;
                                    }catch (InputMismatchException e){
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (gTabs.size() - 1));
                                        s.nextLine();
                                    }
                                }while(true);
                                if (eleccion < 0 || eleccion >= gTabs.size()) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (gTabs.size() - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 0 || eleccion >= gTabs.size());
                                }
                                s.nextLine();

                                grupoActual = gTabs.obtener(eleccion);
                                actual = grupoActual.obtenerUltimo();

                                guardar = true;

                                break;

                            case 4:

                                grupoActual.mostrarLista();

                                System.out.println("Digite el numero de la pestana que quiere ingresar: ");
                                int index;

                                do {
                                    try{
                                        index = s.nextInt();
                                        break;
                                    }catch (InputMismatchException e){
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (grupoActual.size() - 1));
                                        s.nextLine();
                                    }
                                }while(true);
                                if (index < 0 || index >= grupoActual.size()) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                index = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (grupoActual.size() - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (index < 0 || index >= grupoActual.size());
                                }

                                s.nextLine();

                                gTabs.mostrarLista();
                                System.out.println("Digite la posicion de grupo al que la va a meter:");

                                do {
                                    try{
                                        eleccion = s.nextInt();
                                        break;
                                    }catch (InputMismatchException e){
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (gTabs.size() - 1));
                                        s.nextLine();
                                    }
                                }while(true);
                                if (eleccion < 0 || eleccion >= gTabs.size()) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (gTabs.size() - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 0 || eleccion >= gTabs.size());
                                }
                                s.nextLine();

                                Tab aMover = grupoActual.remover(index);
                                gTabs.obtener(eleccion).insertarAlFinal(aMover);

                                break;

                            case 5:
                                gTabs.mostrarLista();
                                System.out.println("\nDigite la posicion de grupo a disolver:");

                                do {
                                    try{
                                        eleccion = s.nextInt();
                                        break;
                                    }catch (InputMismatchException e){
                                        System.out.println("Caracter no valido, seleccione un numero de 0 a " + (gTabs.size() - 1));
                                        s.nextLine();
                                    }
                                }while(true);
                                if (eleccion < 0 || eleccion >= gTabs.size()) {
                                    do {
                                        System.out.println("Seleccione un indice valido");
                                        do {
                                            try {
                                                eleccion = s.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println("Indice no valido, seleccione un numero entre 0 y " + (gTabs.size() - 1));
                                                s.nextLine();
                                            }
                                        } while (true);
                                    } while (eleccion < 0 || eleccion >= gTabs.size());
                                }
                                s.nextLine();

                                if (gTabs.size() == 1){
                                    gTabs.remover();
                                    guardar = true;
                                    salir = true;
                                    break;
                                }

                                if (grupoActual == gTabs.obtener(eleccion)) {
                                    grupoActual = gTabs.obtener(gTabs.size() - 1);
                                }

                                int tam = gTabs.obtener(eleccion).size();
                                for (int i = 0; i < tam; i++){
                                    Tab removido = gTabs.obtener(eleccion).remover();
                                    grupoActual.insertar(removido);
                                }

                                actual = grupoActual.obtenerUltimo();
                                gTabs.remover(eleccion);

                                break;

                            default:
                                System.out.println("Seleccione un numero dentro del rango establecido...");
                                break;
                        }
                    }
                    break;

                case 8:
                    guardar = false;

                    while (!guardar){
                        System.out.println("Mostrando descargas");
                        descargas.mostrarVertical();
                        System.out.println("\n");

                        System.out.println("""
                                1. Agregar descarga.
                                2. Terminar descarga.
                                
                                0. Volver.
                                """);

                        do {
                            try{
                                eleccion = s.nextInt();
                                break;
                            }catch (InputMismatchException e){
                                System.out.println("Caracter no valido, seleccione un numero de 0 a 5");
                                s.nextLine();
                            }
                        }while(true);

                        s.nextLine();
                        switch (eleccion){
                            case 1:
                                System.out.println("Digite el nombre de la descarga que desea agregar.");
                                System.out.println("Titulo: ");
                                String title = s.nextLine();

                                Descarga descarga = new Descarga(title);
                                descargas.insertarAlPrincipio(descarga);
                                break;

                            case 2:
                                if (descargas.size() != 0){
                                    System.out.println("Terminando la primera descarga...");

                                    descargas.borrarUltimo();
                                    break;
                                }
                                System.out.println("No hay descargas activas");
                                break;

                            case 0:
                                System.out.println("Volviendo...");
                                guardar = true;
                                break;

                            default:
                                System.out.println("Seleccione un numero dentro del rango establecido...");
                                break;
                        }
                    }
                    break;

                default:
                    System.out.println("Caracter no valido, seleccione un numero entre 1 y 8.");
                    break;
            }
        }
        System.out.println("Cerrando...");
    }

    private static void listadoMarcadores(Marcador[] marcadores, int i) {
        for (Marcador marcadore : marcadores) {
            if (marcadore.getUrl().isBlank()) break;
            System.out.print(i + "-");
            marcadore.mostrarInfo();
            System.out.println();
            i++;
        }
    }

    public static Marcador[] redimensionarLista(Marcador[] lista, int espacioAdicional){
        Marcador[] listaNueva = new Marcador[lista.length + espacioAdicional];
        System.arraycopy(lista, 0, listaNueva, 0, Math.min(lista.length, listaNueva.length));
        return listaNueva;
    }
}
