# 🎲 B!ngoFácil

O **B!ngoFácil** é um sistema de gerenciamento de bingos desenvolvido com o objetivo de modernizar e expandir o alcance desse tradicional jogo.  
O bingo é amplamente utilizado por comunidades, especialmente as católicas, como meio de arrecadação de recursos.  
No entanto, o formato presencial limita o público participante, reduzindo a renda obtida com a venda das cartelas.

O B!ngoFácil propõe uma **plataforma online** que permite criar, gerenciar e realizar bingos de forma simples, acessível e automatizada.

---

## ⚙️ Funcionalidades

- 🧩 Criar e gerenciar projetos de bingo;
- 🧾 Gerar e exportar cartelas em **PDF** para impressão e distribuição;
- 💻 Realizar a venda de cartelas **online**, alcançando um público mais amplo;
- 🔔 Conduzir o sorteio e o chamamento das pedras **em tempo real** pela plataforma.

---

## 📚 Documentação

Acesse a documentação completa:  
👉 [Documentação no Google Docs](https://docs.google.com/document/d/1UegEH0qPgH0vTd5T1aNsOlAi-fYUU7oV0lDE3-9mL-s/edit?usp=sharing)

Lá você encontrará:
- 📋 Requisitos do sistema;
- 🧠 Especificação de requisitos com **BDD**;
- 📈 Diagramas:
    - UML de Casos de Uso
    - Diagrama de Classes
    - Diagrama Entidade-Relacionamento (DER)

---

## 🛠 Tecnologias Utilizadas

- **Backend:** Spring Boot (Java)
- **Frontend:** Angular
- **Banco de Dados:** H2 (Teste) / PostgreSQL (Produção)
- **Autenticação:** Spring Security com JWT
- **API REST:** JSON
- **Testes:** JUnit / Mockito
- **Gerenciamento de Dependências:** Maven

---

## 💻 Interface Web (Frontend)

O frontend do B!ngoFácil está sendo desenvolvido em **Angular**, oferecendo uma interface intuitiva e moderna para usuários e administradores.  
Ele será responsável por interagir com a API REST, exibir informações dos bingos e gerenciar a experiência visual da plataforma.

🔗 **Template da Interface:** [Acesse o Template Angular](https://github.com/carvalhomaykon/bingo-facil-frontend)

---

## 🗂 Estrutura do Projeto

bingofacil/  
├── src/  
│ ├── main/  
│ │ ├── java/com/bingofacil/bingofacil/  
│ │ │ ├── config/ # Configuração do JWT e segurança  
│ │ │ ├── controllers/ # Controladores REST  
│ │ │ ├── dtos/ # Objetos de transferência de dados  
│ │ │ ├── infra/ # Infraestrutura e suporte  
│ │ │ ├── model/ # Entidades JPA  
│ │ │ ├── repositories/ # Interfaces de acesso ao banco  
│ │ │ ├── security/ # Configuração de autenticação JWT  
│ │ │ └── services/ # Regras de negócio  
│ │ └── resources/  
│ │ └── application.properties  
└── pom.xml  

---

## 🔮 Futuras Implementações

### 🧪 Técnicas
- Implementar **testes automatizados** para cobertura de serviços e controladores.

### ✨ Novas Features
- Dashboard de resultados dos bingos realizados;
- Sistema de ranking de ganhadores;
- Pagamento integrado (**Pix / Cartão**);
- Notificações automáticas por e-mail.

---

## 📜 Licença
Este projeto é de uso educacional e pessoal.  
Caso seja distribuído publicamente, recomenda-se o uso da licença **MIT**.

---

## 👨‍💻 Autor
Desenvolvido por **Maykon Silva Carvalho**  
🎓 Estudante de Sistemas de Informação - UFRA  
📧 [carvalhomaykon566@gmail.com](mailto:carvalhomaykon566@gmail.com)

---

