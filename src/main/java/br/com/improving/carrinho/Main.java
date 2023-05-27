package br.com.improving.carrinho;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Scanner;

/**
 * Classe responsável pela inicializacao do projeto.
 */
public class Main {
	/**
	 * Metodo responsável por compilar o projeto
	 */
	public static void main(String[] args) {
		CarrinhoComprasFactory carrinhoFactory = new CarrinhoComprasFactory();
		Scanner scanner = new Scanner(System.in);

		System.out.print("Informe a identificação do cliente: ");
		String identificacaoCliente = scanner.nextLine();

		CarrinhoCompras carrinho = carrinhoFactory.criar(identificacaoCliente);

		while (true) {
			System.out.println("\nSelecione uma opção:\n" +
					"1 - Adicionar item ao carrinho\n" +
					"2 - Remover item do carrinho\n" +
					"3 - Listar itens do carrinho\n" +
					"4 - Finalizar compra\n");

			int opcao;
			if (scanner.hasNextInt()) {
				opcao = scanner.nextInt();
				scanner.nextLine();
			} else {
				System.out.println("Opção inválida! Por favor, insira um número válido.");
				scanner.nextLine();
				continue;
			}

			switch (opcao) {
				case 1:
					System.out.print("Informe o código do produto: ");
					Long codigoProduto;
					if (scanner.hasNextLong()) {
						codigoProduto = scanner.nextLong();
						scanner.nextLine();
					} else {
						System.out.println("Código do produto inválido! Por favor, insira um número válido.");
						scanner.nextLine();
						continue;
					}

					boolean codigoExistente = false;
					for (Item item : carrinho.getItens()) {
						if (item.getProduto().getCodigo().equals(codigoProduto)) {
							codigoExistente = true;
							break;
						}
					}

					if (codigoExistente) {
						System.out.println("O produto com código " + codigoProduto + " já está no carrinho. Por favor, informe outro código.");
						continue;
					} else {

						System.out.print("Informe a descrição do produto: ");
						String descricaoProduto = scanner.nextLine();

						System.out.print("Informe o valor unitário do produto: ");
						BigDecimal valorUnitario;
						scanner.useLocale(Locale.US);
						if (scanner.hasNextInt()) {
							int valorInteiro = scanner.nextInt();
							valorUnitario = BigDecimal.valueOf(valorInteiro);
							scanner.nextLine();
						} else if (scanner.hasNextFloat()) {
							float valorFloat = scanner.nextFloat();
							valorUnitario = BigDecimal.valueOf(valorFloat);
							scanner.nextLine();
						} else {
							String input = scanner.nextLine();
							String valorString = input.replace(",", ".");
							try {
								valorUnitario = new BigDecimal(valorString);
							} catch (NumberFormatException e) {
								System.out.println("Valor unitário inválido! Por favor, insira um número válido.");
								continue;
							}
						}

						System.out.print("Informe a quantidade: ");
						int quantidade;
						if (scanner.hasNextInt()) {
							quantidade = scanner.nextInt();
							scanner.nextLine();
						} else {
							System.out.println("Quantidade inválida! Por favor, insira um número válido.");
							scanner.nextLine();
							continue;
						}

						Item item = new Item(new Produto(codigoProduto, descricaoProduto), valorUnitario, quantidade);
						carrinho.adicionarItem(item.getProduto(), item.getValorUnitario(), item.getQuantidade());
					}
					break;

				case 2:
					System.out.print("Informe o código do produto: ");
					Long codigoProdutoRemover;
					if (scanner.hasNextLong()) {
						codigoProdutoRemover = scanner.nextLong();
						scanner.nextLine();
					} else {
						System.out.println("Código do produto inválido! Por favor, insira um número válido.");
						scanner.nextLine();
						continue;
					}
					carrinho.removerItem(new Produto(codigoProdutoRemover, ""));
					break;

				case 3:
					System.out.println("Itens no carrinho:");
					for (Item itens : carrinho.getItens()) {
						BigDecimal valorTotal = itens.getValorTotal().setScale(2, RoundingMode.HALF_UP);
						System.out.println("- " + itens.getQuantidade() + "x " + itens.getProduto().getDescricao() + " (" +
								itens.getValorUnitario().setScale(2, RoundingMode.HALF_UP) + " cada) - Total: " + valorTotal);
					}
					break;
				case 4:
					System.out.println("Compra finalizada!\n Valor total: " + carrinho.getValorTotal().setScale(2, RoundingMode.HALF_UP));
					scanner.close();
					return;

				default:
					System.out.println("Opção inválida!");
					break;
			}
		}
	}
}
