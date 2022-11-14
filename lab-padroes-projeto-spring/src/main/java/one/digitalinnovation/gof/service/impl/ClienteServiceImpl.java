package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 *
 * @author silvanors
 * */

@Service
public class ClienteServiceImpl implements ClienteService {

    // Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired ClienteRepository clienteRepository;
    @Autowired EnderecoRepository enderecoRepository;

    @Autowired ViaCepService viaCepService;

    // Strategy: Implementar os médotos definidos na interface.
    // Facade: Abstrar integrações com subsistemas, promovendo uma interface simples.

    @Override
    public Iterable<Cliente> buscarTodos() {
        // Buscar todos os clientes
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        // Buscar Cliente por ID.
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);

    }

    @Override
    public void atualizar (Long id, Cliente cliente) {
        // Buscar Cliente por ID, caso exista:
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            // Verificar se o Endereço do Cliente já existe (pelo CEP).
            // Caso não exista, integrar com o ViaCEP e persistir o retorno.
            // Alterar Cliente, vinculando o Endereco (novo ou existente).
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        // Deletar Cliente por ID.
        clienteRepository.deleteById(id);
    }

    //Método privado para usar em duas funcionalidades nesta classe.
    //Método utilizado para deixar o código mais limpo.
    //Métodos privados devem ser organizados no final da classe (por último)
    private void salvarClienteComCep(Cliente cliente) {
        //  Verificar se o Endereco do Cliente já existe (pelo CEP).
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            //  Caso não exista, integrar com o ViaCEP e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        //  Inserir Cliente, vinculando o Endereco (novo ou existente)
        clienteRepository.save(cliente);
    }
}
