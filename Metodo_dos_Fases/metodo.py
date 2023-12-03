from pulp import *
from os import system
import time

class ProgramaPL:
    def __init__(self):
        self.limpiar_consola()
        self.numero_restriccion = 1  # Inicializar el contador de restricciones

    def limpiar_consola(self):
        system("cls")

    def obtener_entrada_numerica(self, prompt):
        while True:
            try:
                return float(input(prompt))
            except ValueError:
                print("Por favor, ingrese un valor numérico válido.")

    def crear_problema_pl(self, tipo_problema):
        return LpProblem("Problema", LpMinimize if tipo_problema == "min" else LpMaximize)

    def definir_variables(self, num_variables):
        return [0] + [LpVariable(f"X{i}", lowBound=0) for i in range(1, num_variables + 1)]

    def definir_funcion_objetivo(self, variables):
        print("\nFunción objetivo: ")
        funcion_objetivo = lpSum(self.obtener_entrada_numerica(f"Digite el coeficiente de {var.name}: ") * var for var in variables[1:])
        return funcion_objetivo

    def definir_restriccion(self, variables):
        coeficientes = [self.obtener_entrada_numerica(f"Digite el coeficiente de {var.name}:  ") * var for var in variables[1:]]
        rhs = self.obtener_entrada_numerica("Digite la solucion de la restricción: ")

        while True:
            signo_restriccion = input("Digite el símbolo de la restricción (<=, >=, =): ")
            if signo_restriccion in ["<=", ">=", "="]:
                break
            else:
                print("Error: Por favor, ingrese uno de los símbolos válidos (<=, >=, =)")

        nombre_restriccion = f'Restricción {self.numero_restriccion}'  # Nombre de la restricción
        self.numero_restriccion += 1  # Incrementar el contador de restricciones

        if signo_restriccion == "<=":
            return lpSum(coeficientes) <= rhs, nombre_restriccion
        elif signo_restriccion == ">=":
            return lpSum(coeficientes) >= rhs, nombre_restriccion
        elif signo_restriccion == "=":
            return lpSum(coeficientes) == rhs, nombre_restriccion

    def imprimir_problema(self, problema):
        self.limpiar_consola()
        print("\n.:Problema a resolver:.\n")
        print('Funcion objetivo:')
        print(f'Z = {problema.objective}')

        print('\nRestricciones:')
        for nombre, restriccion in problema.constraints.items():
            print(f'{nombre}: {restriccion}')

    def resolver_problema(self, problema, variables):
        if problema.solve(PULP_CBC_CMD(msg=False)) == 1:
            print("\nSolución óptima en: ")
            for i, var in enumerate(variables[1:], start=1):
                print(f"{var.name} = {var.varValue}")
            print("\nCon un valor de Z =", value(problema.objective), "\n")
        else:
            print("\nNo se encontró una solución para el problema planteado.\n")

    def ejecutar(self):
        while True:
            self.limpiar_consola()
            print("Programa desarrollado por David Aldana - Ivan Bejarano")
            tipo_problema = input("Digite el tipo de problema (min/max): ").lower()
            if tipo_problema not in ["min", "max"]:
                print("Error: Por favor, ingrese 'min' o 'max' como el tipo de problema.")
                continue

            problema = self.crear_problema_pl(tipo_problema)

            num_variables = int(self.obtener_entrada_numerica("\nDigite el número de variables del problema: "))
            variables = self.definir_variables(num_variables)

            problema += self.definir_funcion_objetivo(variables)

            num_restricciones = int(self.obtener_entrada_numerica("Digite el numero de restricciones del problema: "))

            for _ in range(num_restricciones):
                restriccion, nombre_restriccion = self.definir_restriccion(variables)
                problema += restriccion
                print(f'{nombre_restriccion}: {restriccion}')

            self.imprimir_problema(problema)

            if input("\n¿Este es el problema que desea Resolver? (s/n): ").lower() == 's':
                break
            else:
                print("Por favor, vuelva a digitar su problema de programación lineal.")

        self.resolver_problema(problema, variables)
        print("\nPresione Enter para cerrar el programa.")
        input()
        

if __name__ == "__main__":
    programa = ProgramaPL()
    programa.ejecutar()
   