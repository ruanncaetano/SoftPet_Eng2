export class UserModel
{
    private id?: number;
    private cpf: string;
    private senha: string;

    constructor(cpf: string, senha: string, id?: number)
    {
        this.id = id;
        this.cpf = cpf;
        this.senha = senha;
    }

    public getId(): number | undefined
    {
        return this.id;
    }
    public setId(id: number): void
    {
        this.id = id;
    }

    public getCpf(): string
    {
        return this.cpf;
    }
    public setCpf(cpf: string): void
    {
        this.cpf = cpf
    }

    public getSenha(): string
    {
        return this.senha;
    }
    public setSenha(senha: string): void
    {
        this.senha = senha;
    }
}