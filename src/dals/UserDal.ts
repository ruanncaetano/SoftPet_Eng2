import { Pool } from 'pg';
import { UserModel } from '../models/UserModel';
import { DataBase } from '../config/DataBase'; // Singleton de conexão

export class UserDAL {
  private db: Pool;

  constructor() {
    this.db = DataBase.getInstance(); // Usa Singleton para o Pool
  }

  async findByCPF(cpf: string): Promise<UserModel | null> {
    const result = await this.db.query('SELECT * FROM credenciais WHERE cpf = $1', [cpf]);
    if (result.rows.length === 0) return null;

    const row = result.rows[0];
    return new UserModel(row.id, row.cpf, row.senha);
  }

  async create(user: UserModel): Promise<UserModel>
  {
    const result = await this.db.query(
        'INSERT INTO credenciais (cpf, senha) VALUES ($1, $2) RETURNING id',
        [user.getCpf(), user.getSenha()]
    );
    const insertedId = result.rows[0].id;
    return new UserModel(user.getCpf(), user.getSenha(), insertedId);
  }

  //update
  async updateSenha(cpf: string, novaSenha: string): Promise<void>
  {
    await this.db.query(
      'UPDATE users SET senha = $1 WHERE cpf = $2',
      [novaSenha, cpf]
    );
  }

  //delete
  async deleteByCPF(cpf: string): Promise<void>
  {
    await this.db.query('DELETE FROM credenciais WHERE cpf = $1', [cpf]);
  }
}
