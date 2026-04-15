package com.teste.infilex.main;

import com.teste.infilex.model.Funcionario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Principal {

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Locale LOCALE_BR = new Locale("pt", "BR");
    private static final NumberFormat FORMATADOR_NUMERO = NumberFormat.getNumberInstance(LOCALE_BR);
    private static final NumberFormat FORMATADOR_MOEDA = NumberFormat.getCurrencyInstance(LOCALE_BR);

    public static void main(String[] args) {
        FORMATADOR_NUMERO.setMinimumFractionDigits(2);
        FORMATADOR_NUMERO.setMaximumFractionDigits(2);

        List<Funcionario> funcionarios = new ArrayList<>();

        funcionarios.add(new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente"));

        funcionarios.removeIf(funcionario -> funcionario.getNome().equalsIgnoreCase("João"));

        System.out.println("Funcionários:");
        imprimirFuncionarios(funcionarios);

        aplicarAumento(funcionarios, new BigDecimal("0.10"));
        System.out.println("\nFuncionários com 10% de aumento:");
        imprimirFuncionarios(funcionarios);

        Map<String, List<Funcionario>> funcionariosPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

        System.out.println("\nFuncionários agrupados por função:");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("\nFunção: " + funcao);
            lista.forEach(Principal::imprimirFuncionario);
        });

        System.out.println("\nAniversariantes dos meses 10 e 12:");
        funcionarios.stream()
                .filter(funcionario -> {
                    int mes = funcionario.getDataNascimento().getMonthValue();
                    return mes == 10 || mes == 12;
                })
                .forEach(Principal::imprimirFuncionario);

        System.out.println("\nFuncionário com maior idade:");
        Funcionario maisVelho = funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .orElse(null);

        if (maisVelho != null) {
            int idade = Period.between(maisVelho.getDataNascimento(), LocalDate.now()).getYears();
            System.out.println("Nome: " + maisVelho.getNome());
            System.out.println("Idade: " + idade + " anos");
        }

        System.out.println("\nFuncionários em ordem alfabética:");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome))
                .forEach(Principal::imprimirFuncionario);

        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("\nTotal dos salários: " + FORMATADOR_MOEDA.format(totalSalarios));

        BigDecimal salarioMinimo = new BigDecimal("1212.00");
        System.out.println("\nQuantidade de salários mínimos por funcionário:");
        funcionarios.forEach(funcionario -> {
            BigDecimal qtdSalariosMinimos = funcionario.getSalario()
                    .divide(salarioMinimo, 2, RoundingMode.HALF_UP);

            System.out.println(
                    "Nome: " + funcionario.getNome()
                            + " | Salários mínimos: " + FORMATADOR_NUMERO.format(qtdSalariosMinimos)
            );
        });
    }

    private static void aplicarAumento(List<Funcionario> funcionarios, BigDecimal percentual) {
        for (Funcionario funcionario : funcionarios) {
            BigDecimal aumento = funcionario.getSalario().multiply(percentual);
            BigDecimal novoSalario = funcionario.getSalario().add(aumento);
            funcionario.setSalario(novoSalario);
        }
    }

    private static void imprimirFuncionarios(List<Funcionario> funcionarios) {
        funcionarios.forEach(Principal::imprimirFuncionario);
    }

    private static void imprimirFuncionario(Funcionario funcionario) {
        System.out.println(
                "Nome: " + funcionario.getNome()
                        + " | Data Nascimento: " + funcionario.getDataNascimento().format(FORMATADOR_DATA)
                        + " | Salário: " + FORMATADOR_NUMERO.format(funcionario.getSalario())
                        + " | Função: " + funcionario.getFuncao()
        );
    }
}