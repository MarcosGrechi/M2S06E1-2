package petshop.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import petshop.models.Pet;

@WebServlet("/pet/*")
public class PetServlet extends HttpServlet {
    private static Map<Integer, Pet> pets = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            writer.println("Lista de todos os pets:");
            for (Pet pet : pets.values()) {
                writer.println(pet);
            }
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    int id = Integer.parseInt(splits[1]);
                    Pet pet = pets.get(id);
                    if (pet != null) {
                        writer.println("Detalhes do pet: " + pet);
                    } else {
                        writer.println("Pet com ID " + id + " não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    writer.println("ID inválido.");
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String nome = req.getParameter("nome");
        String especie = req.getParameter("especie");
        String raca = req.getParameter("raca");
        int idade = Integer.parseInt(req.getParameter("idade"));
        float peso = Float.parseFloat(req.getParameter("peso"));
        String sexo = req.getParameter("sexo");

        Pet novoPet = new Pet(nome, especie, raca, idade, peso, sexo);
        pets.put(novoPet.getId(), novoPet);

        PrintWriter writer = resp.getWriter();
        writer.println("Pet cadastrado com sucesso: " + novoPet);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Pet pet = pets.get(id);
        if (pet != null) {
            String nome = req.getParameter("nome");
            String especie = req.getParameter("especie");
            String raca = req.getParameter("raca");
            String idadeStr = req.getParameter("idade");
            String pesoStr = req.getParameter("peso");
            String sexo = req.getParameter("sexo");

            // Verifica e aplica as atualizações apenas para os campos não nulos ou vazios
            if (nome != null && !nome.isEmpty()) {
                pet.setNome(nome);
            }
            if (especie != null && !especie.isEmpty()) {
                pet.setEspecie(especie);
            }
            if (raca != null && !raca.isEmpty()) {
                pet.setRaca(raca);
            }
            if (idadeStr != null && !idadeStr.isEmpty()) {
                int idade = Integer.parseInt(idadeStr);
                pet.setIdade(idade);
            }
            if (pesoStr != null && !pesoStr.isEmpty()) {
                float peso = Float.parseFloat(pesoStr);
                pet.setPeso(peso);
            }
            if (sexo != null && !sexo.isEmpty()) {
                pet.setSexo(sexo);
            }

            PrintWriter writer = resp.getWriter();
            writer.println("Pet com ID " + id + " atualizado com sucesso: " + pet);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.println("Pet com ID " + id + " não encontrado para atualização.");
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    int id = Integer.parseInt(splits[1]);
                    Pet removedPet = pets.remove(id);
                    PrintWriter writer = resp.getWriter();
                    if (removedPet != null) {
                        writer.println("Pet com ID " + id + " excluído com sucesso.");
                    } else {
                        writer.println("Pet com ID " + id + " não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    PrintWriter writer = resp.getWriter();
                    writer.println("ID inválido.");
                }
            }
        }
    }
}
