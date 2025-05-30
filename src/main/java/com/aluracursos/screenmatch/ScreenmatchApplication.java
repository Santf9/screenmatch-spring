package com.aluracursos.screenmatch;

import com.aluracursos.screenmatch.main.Main;
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

		// Instancia de la clase Main y mostrar el menú
		Main main = new Main();
		main.mostrarMenu();

    // EjemploStream ejemploStream = new EjemploStream();
  	// ejemploStream.ejemploStream();

	}
}
