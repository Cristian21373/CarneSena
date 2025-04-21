package com.proyecto.carnesena.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.proyecto.carnesena.interfaces.Iadmin;
import com.proyecto.carnesena.model.admin;
import com.proyecto.carnesena.model.role;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private Iadmin adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminInitializer(Iadmin adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            admin admin = new admin();
            admin.setUsername("cristian.bahamon@soy.sena.edu.co");
            admin.setFirst_name("Cristian");
            admin.setLast_name("pasante");
            admin.setRole(role.SUPERADMIN);
            admin.setPassword(passwordEncoder.encode("Crisdakar2137.."));
            adminRepository.save(admin);
            
            admin nuevoAdmin = new admin();
            nuevoAdmin.setUsername("Matriculas9527@sena.edu.co"); 
            nuevoAdmin.setFirst_name("Nuevo");
            nuevoAdmin.setLast_name("Administrador"); 
            nuevoAdmin.setRole(role.SUPERADMIN); 
            nuevoAdmin.setPassword(passwordEncoder.encode("GitaEmma.2025*")); 
            adminRepository.save(nuevoAdmin);
        }
    }
}