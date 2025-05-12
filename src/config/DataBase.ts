import dotenv from 'dotenv'; 
dotenv.config();  //carrega as variáveis do arquivo .env

import { Pool } from 'pg';

//usamos a conexão via variáveis de ambiente
export class DataBase 
{
  private static instance: Pool;

  //método getInstance retorna a instância do Pool
  public static getInstance(): Pool 
  {
    if(!DataBase.instance) 
    {
        DataBase.instance = new Pool({
          user: process.env.PG_USER,         
          host: process.env.PG_HOST,          
          database: process.env.PG_DATABASE,  
          password: process.env.PG_PASSWORD,  
          port: Number(process.env.PG_PORT),
      });
    }
    return DataBase.instance;
  }
}
