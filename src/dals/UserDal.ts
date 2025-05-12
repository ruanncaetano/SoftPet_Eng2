import { Pool } from 'pg';
import { UserModel } from '../models/UserModel';
import { DataBase } from '../config/DataBase'; // Singleton de conexão

export class UserDAL 
{
  private db: Pool;

  constructor()
  {
    this.db = DataBase.getInstance(); // Usa Singleton para o Pool
  }

  //buscando por um usuario pelo cpf
  async findByCPF(cpf: string): Promise<UserModel | null>
  {
    const result = await this.db.query('SELECT * FROM credenciais WHERE cre_login = $1', [cpf]);
    if(result.rows.length != 0)
    {
        const row = result.rows[0];
        return new UserModel(row.cre_login,row.cre_senha,row.cre_cod);
    } 
    return null;
  }

  async create(user: UserModel): Promise<UserModel>
  {
    const result = await this.db.query(
        'INSERT INTO credenciais (cre_login, cre_senha) VALUES ($1, $2) RETURNING cre_cod',
        [user.getCpf(), user.getSenha()]
    );
    const insertedId = result.rows[0].cre_cod;
    return new UserModel(user.getCpf(), user.getSenha(), insertedId);
  }

  //update
  async updateSenha(cpf: string, novaSenha: string): Promise<void>
  {
    await this.db.query('UPDATE credenciais SET cre_senha = $1 WHERE cre_login = $2',[novaSenha, cpf]);
  }

  //delete
  async deleteByCPF(cpf: string): Promise<void>
  {
    await this.db.query('DELETE FROM credenciais WHERE cre_login = $1', [cpf]);
  }
}
