package br.com.beertechtalents.lupulo.pocmq.service;

import br.com.beertechtalents.lupulo.pocmq.controller.exception.EntityNotFoundException;
import br.com.beertechtalents.lupulo.pocmq.model.Conta;
import br.com.beertechtalents.lupulo.pocmq.model.Operacao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransferenciaService {

    ContaService contaService;
    OperacaoService operacaoService;

    @Transactional
    public void transferir(UUID origem, UUID destino, BigDecimal valor) {
        Optional<Conta> optionalContaOrigem = contaService.getConta(origem);
        if (optionalContaOrigem.isEmpty()) {
            throw new EntityNotFoundException(Conta.class, "uuid", origem);
        }

        Operacao op = new Operacao();
        op.setValor(valor);
        op.setTipo(Operacao.TipoTransacao.SAQUE);
        op.setDescricaoOperacao(Operacao.DescricaoOperacao.TRANSFERENCIA_ORIGEM);
        op.setConta(optionalContaOrigem.get());
        operacaoService.salvarOperacao(op);

        Optional<Conta> optionalContaDestino = contaService.getConta(destino);
        if (optionalContaDestino.isEmpty()) {
            throw new EntityNotFoundException(Conta.class, "uuid", destino);
        }

        op = new Operacao();
        op.setValor(valor);
        op.setTipo(Operacao.TipoTransacao.DEPOSITO);
        op.setDescricaoOperacao(Operacao.DescricaoOperacao.TRANSFERENCIA_DESTINO);
        op.setConta(optionalContaDestino.get());
        operacaoService.salvarOperacao(op);

    }
}
