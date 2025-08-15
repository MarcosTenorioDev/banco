# Documentação do Projeto Banco Android

## Sobre o Projeto

Este é um aplicativo Android que simula um sistema bancário básico. O projeto foi desenvolvido usando a arquitetura MVVM com Room Database para persistência local. O app permite gerenciar contas bancárias, fazer operações como crédito, débito e transferência, além de registrar e consultar transações.

## O que foi implementado

### Item 1 - ContasActivity
Implementei a recuperação de dados do banco usando o ContaViewModel. A tela agora mostra a lista de contas atualizada automaticamente quando há mudanças no banco de dados. Usei o LiveData para observar as mudanças e o RecyclerView para exibir a lista de forma eficiente.

```java
viewModel.contas.observe(this, todasContas -> {
    adapter.submitList(todasContas);
});
```

### Item 2 - ContaViewHolder (Imagem baseada no saldo)
Adicionei lógica para mudar o ícone da conta baseado no saldo. Se o saldo for negativo, mostra o ícone de delete (vermelho), se não, mostra o ícone OK (verde). Isso ajuda a identificar visualmente contas com problemas.

```java
if (c.saldo < 0) {
    this.icone.setImageResource(R.drawable.delete);
} else {
    this.icone.setImageResource(R.drawable.ok);
}
```

### Item 3 - ContaViewHolder (Botão remover)
Configurei o botão de deletar para chamar o método remover do ViewModel. Tive que instanciar o ViewModel dentro do listener para ter acesso ao contexto correto.

```java
    ContaViewModel viewModel = new ViewModelProvider((androidx.activity.ComponentActivity) this.context).get(ContaViewModel.class);
    viewModel.remover(c);
```

### Item 4 - ContaViewHolder (Intent com número da conta)
Quando o usuário clica em editar, o app agora passa o número da conta como extra no Intent. Isso permite que a tela de edição saiba qual conta carregar.

```java
Intent i = new Intent(this.context, EditarContaActivity.class);
i.putExtra("numeroDaConta", c.numero);
```

### Item 5 - AdicionarContaActivity (Validação)
Implementei validações completas para todos os campos:
- Nome obrigatório com mínimo de 5 caracteres
- CPF obrigatório
- Número da conta obrigatório
- Saldo obrigatório e deve ser um número válido maior que zero

Se alguma validação falha, o app mostra uma mensagem específica e foca no campo com problema.

*Ex:*
```java
    if (saldoConta.isEmpty()) {
        Toast.makeText(this, "Saldo é obrigatório!", Toast.LENGTH_SHORT).show();
        campoSaldo.requestFocus();
        return;
    }
```

### Item 6 - ContaDAO
Adicionei os métodos de update e delete, além de três queries de busca:
- Busca por número da conta (retorna uma conta única)
- Busca por nome do cliente (busca parcial com LIKE)
- Busca por CPF (busca exata)

Usei as anotações @Update e @Delete do Room, e @Query para as buscas personalizadas.

### Item 7 - ContaRepository
Implementei os métodos do repository usando @WorkerThread para garantir que as operações de banco rodem em background. O repository encapsula as operações do DAO e fornece uma interface mais limpa.

### Item 8 - ContaViewModel
Implementei métodos simples para as operações de CRUD e busca. Todos os métodos seguem o mesmo padrão, rodando em thread separada para não travar a UI. Adicionei também o método getRepository() para permitir acesso direto ao repository quando necessário.

```java
void buscarPeloNome(String nomeCliente) {
    new Thread(() -> repository.buscarPeloNome(nomeCliente)).start();
}

public ContaRepository getRepository() {
    return repository;
}
```

### Item 9 - EditarContaActivity (Recuperar dados)
A tela recupera o número da conta do Intent e busca os dados diretamente do repository em uma thread separada. Os campos são preenchidos automaticamente quando os dados chegam, usando runOnUiThread() para atualizar a UI na thread principal.

```java
    new Thread(() -> {
        contaAtual = viewModel.getRepository().buscarPeloNumero(numeroConta);
        runOnUiThread(() -> {
            if (contaAtual != null) {
                campoNome.setText(contaAtual.nomeCliente);
                campoNumero.setText(contaAtual.numero);
                campoCPF.setText(contaAtual.cpfCliente);
                campoSaldo.setText(String.valueOf(contaAtual.saldo));
            }
        });
    }).start();           
```

### Item 10 - EditarContaActivity (Validação e atualização)
Reutilizei as mesmas validações do Item 5 para manter consistência. Quando a validação passa, crio um novo objeto Conta e chamo o método atualizar do ViewModel.

### Item 11 - EditarContaActivity (Botão remover)
O botão de remover usa a variável contaAtual que foi carregada anteriormente e chama o método remover do ViewModel. Mostra uma mensagem de sucesso e fecha a tela.

```java
btnRemover.setOnClickListener(v -> {
    if (contaAtual != null) {
        viewModel.remover(contaAtual);
        Toast.makeText(this, "Conta removida com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
});
```

### Item 12 - BancoViewModel
Implementei os métodos para as operações bancárias (transferir, creditar, debitar) e buscas. Cada operação roda em thread separada e verifica se as contas existem antes de fazer as alterações.

### Item 13 - Activities de Operações
Todas as telas de operação (CreditarActivity, DebitarActivity, TransferirActivity) agora têm validação completa e criam automaticamente um registro de transação após a operação bem-sucedida.

### Item 14 - PesquisarActivity (Lógica de busca)
A tela de pesquisa verifica qual tipo de busca foi selecionado (por nome, CPF ou número da conta) e chama o método apropriado.

### Item 15 - PesquisarActivity (Atualização do RecyclerView)
As buscas rodam em thread separada e atualizam o RecyclerView na thread principal usando runOnUiThread(). Se nenhum resultado é encontrado, mostra uma mensagem para o usuário.

### Item 16 - MainActivity (Valor total do banco)
A tela principal agora mostra o valor total de dinheiro no banco, somando todos os saldos das contas. O valor é atualizado automaticamente quando há mudanças.

```java
viewModel.getContaRepository().getContas().observe(this, listaContas -> {
    double total = 0.0;
    for (Conta conta : listaContas) {
        total += conta.saldo;
    }
    String totalFormatado = String.format("R$ %.2f", total);
    totalBanco.setText(totalFormatado);
});
```

### Item 17 - TransacaoViewHolder (Cores diferenciadas)
As transações de débito agora aparecem em vermelho, enquanto créditos aparecem em azul. Isso ajuda a identificar visualmente o tipo de transação.

```java
if (t.tipoTransacao == 'D') {
    this.valorTransacao.setTextColor(Color.RED);
} else {
    this.valorTransacao.setTextColor(Color.BLUE);
}
```

### Item 18 - TransacaoDAO
Implementei métodos de busca para transações:
- Por número da conta
- Por data
- Por data e tipo (crédito ou débito)
- Por número da conta e tipo

Todas as queries ordenam por data (mais recentes primeiro).

### Item 19 - TransacaoRepository
Adicionei os métodos do repository com @WorkerThread para as operações de banco, seguindo os exemplos anteriores. O repository encapsula as operações do DAO.

### Item 20 - TransacaoViewModel
Implementei métodos simples para as operações de busca de transações, seguindo o mesmo padrão do ContaViewModel. Todos os métodos rodam em thread separada para não travar a UI.

```java
void buscarTransacoesPelaData(String data) {
    new Thread(() -> repository.buscarPelaData(data)).start();
}
```

### Item 21 - TransacoesActivity
A tela de transações agora permite buscar por data ou número da conta, com filtros opcionais por tipo de transação (crédito/débito). Os resultados são exibidos no RecyclerView e atualizados automaticamente.


## Items opcionais:

### Fazer melhorias gerais de UI na aplicação.
#### 1. Tela Principal (MainActivity)
- Layout em grid 2x3 para melhor organização
- Botões coloridos com ícones emoji
- Card destacado para o total do banco
#### 2. Lista de Contas
- Cards para as contas
- Ícones de status com background circular
- Botões de editar e excluir com cores diferenciadas
- Efeito de profundidade
#### 3. Formulários
- Header informativo com ícones
- Campos com background diferente do fundo
- emojis
#### 4. Operações Bancárias e histórico de transações,
- Cards
- Cores destacando items principais
- RadioButtons com ícones e cores
#### 5. Componetização personalizada
- button_primary.xml - Botão azul principal
- button_secondary.xml - Botão roxo
- button_danger.xml - Botão vermelho
- button_success.xml - Botão verde
- button_info.xml - Botão azul claro
- button_warning.xml - Botão laranja
- button_dark.xml - Botão cinza
- edit_text_background.xml - Background dos campos
- icon_background.xml - Background dos ícones
- valor_background.xml - Background dos valores