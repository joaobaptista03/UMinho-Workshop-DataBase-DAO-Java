CREATE DATABASE IF NOT EXISTS OficinaDB;

USE OficinaDB;

CREATE TABLE IF NOT EXISTS veiculos (
    matricula VARCHAR(20) PRIMARY KEY,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    tipo_motor VARCHAR(20),
    informacoes TEXT
);

CREATE TABLE IF NOT EXISTS clientes (
    id INT PRIMARY KEY,
    nome VARCHAR(100),
    nif INT,
    morada VARCHAR(255),
    email VARCHAR(100),
    password VARCHAR(100),
    telefone INT
);

CREATE TABLE IF NOT EXISTS faturas (
    nrFatura INT PRIMARY KEY,
    clienteId INT,
    FOREIGN KEY (clienteId) REFERENCES clientes(id)
);

CREATE TABLE IF NOT EXISTS funcionarios (
    id INT PRIMARY KEY,
    nome VARCHAR(100),
    nrCartao INT,
    posto VARCHAR(50),
    competencias TEXT
);

CREATE TABLE IF NOT EXISTS servicos (
    id INT PRIMARY KEY,
    estado VARCHAR(50),
    dataHora DATETIME,
    funcionarioId INT,
    faturaNr INT,
    veiculoMatricula VARCHAR(20),
    servicoTipo VARCHAR(50),
    FOREIGN KEY (funcionarioId) REFERENCES funcionarios(id),
    FOREIGN KEY (faturaNr) REFERENCES faturas(nrFatura),
    FOREIGN KEY (veiculoMatricula) REFERENCES veiculos(matricula)
);