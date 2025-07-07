//package com.aluracursos.screenmatch;
//
//import com.aluracursos.screenmatch.main.Main;
//import com.aluracursos.screenmatch.repository.SerieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class ScreenmatchApplicationConsola implements CommandLineRunner {
//
//	// Inyectar el repositorio de Serie para poder interactuar con la base de datos - Inyección de dependencias
//	@Autowired
//	private SerieRepository repository;
//
//	public static void main(String[] args) {
//		SpringApplication.run(ScreenmatchApplicationConsola.class, args);
//	}
//
//	@Override
//	public void run(String... args) {
//		//System.out.println("Hola, Spring Screenmatch!");
//
//		// Instancia de la clase MainObtenerDatos y mostrar el menú
////		MainObtenerDatosSerie mainObtenerDatos = new MainObtenerDatosSerie();
////		mainObtenerDatos.mostrarMenu();
//
//		Main main = new Main(repository); // Instancia de la clase Main con el repositorio inyectado
//		main.mostrarMenu();
//	}
//}
