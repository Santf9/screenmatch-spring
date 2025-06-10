package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.main.Main;
import com.aluracursos.screenmatch.main.MainObtenerDatosSerie;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//System.out.println("Hola, Spring Screenmatch!");

		// Instancia de la clase MainObtenerDatos y mostrar el menú
//		MainObtenerDatosSerie mainObtenerDatos = new MainObtenerDatosSerie();
//		mainObtenerDatos.mostrarMenu();

		Main main = new Main();
		main.mostrarMenu();


	}
}
