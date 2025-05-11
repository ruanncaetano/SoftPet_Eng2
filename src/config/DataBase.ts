import dotenv from 'dotenv'; 
dotenv.config();  // Carrega as variáveis do arquivo .env

import { Pool } from 'pg';

// Aqui, usamos a conexão via variáveis de ambiente
export class DataBase {
  private static instance: Pool;

  // O método getInstance retorna a instância do Pool
  public static getInstance(): Pool {
    if (!DataBase.instance) {
      DataBase.instance = new Pool({
        user: process.env.PG_USER,          // Usuário do PostgreSQL
        host: process.env.PG_HOST,          // Host do banco (ex: localhost)
        database: process.env.PG_DATABASE,  // Nome do banco de dados
        password: process.env.PG_PASSWORD,  // Senha do usuário
        port: Number(process.env.PG_PORT),  // Porta do PostgreSQL
      });
    }
    return DataBase.instance;
  }
}
