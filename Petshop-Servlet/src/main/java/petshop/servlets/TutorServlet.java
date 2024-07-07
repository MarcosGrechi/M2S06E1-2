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
import petshop.models.Tutor;

@WebServlet("/tutor/*")
public class TutorServlet extends HttpServlet {
    private static Map<Integer, Tutor> tutores = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        PrintWriter writer = resp.getWriter();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            writer.println("Lista de todos os tutores:");
            for (Tutor tutor : tutores.values()) {
                writer.println(tutor);
            }
        } else {
            String[] splits = pathInfo.split("/");
            if (splits.length == 2) {
                try {
                    int id = Integer.parseInt(splits[1]);
                    Tutor tutor = tutores.get(id);
                    if (tutor != null) {
                        writer.println("Detalhes do tutor: " + tutor);
                    } else {
                        writer.println("Tutor com ID " + id + " não encontrado.");
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
        String telefone = req.getParameter("telefone");
        String endereco = req.getParameter("endereco");

        Tutor novoTutor = new Tutor(nome, telefone, endereco);
        tutores.put(novoTutor.getId(), novoTutor);

        PrintWriter writer = resp.getWriter();
        writer.println("Tutor cadastrado com sucesso: " + novoTutor);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        Tutor tutor = tutores.get(id);
        if (tutor != null) {
            String nome = req.getParameter("nome");
            String telefone = req.getParameter("telefone");
            String endereco = req.getParameter("endereco");

            // Verifica e aplica as atualizações apenas para os campos não nulos ou vazios
            if (nome != null && !nome.isEmpty()) {
                tutor.setNome(nome);
            }
            if (telefone != null && !telefone.isEmpty()) {
                tutor.setTelefone(telefone);
            }
            if (endereco != null && !endereco.isEmpty()) {
                tutor.setEndereco(endereco);
            }

            PrintWriter writer = resp.getWriter();
            writer.println("Tutor com ID " + id + " atualizado com sucesso: " + tutor);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.println("Tutor com ID " + id + " não encontrado para atualização.");
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
                    Tutor removedTutor = tutores.remove(id);
                    PrintWriter writer = resp.getWriter();
                    if (removedTutor != null) {
                        writer.println("Tutor com ID " + id + " excluído com sucesso.");
                    } else {
                        writer.println("Tutor com ID " + id + " não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    PrintWriter writer = resp.getWriter();
                    writer.println("ID inválido.");
                }
            }
        }
    }
}
