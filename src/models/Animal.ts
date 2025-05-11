export class Animal{
    cod: number;
    nome: String;
    idade: number;
    tipo: string;
    sexo: string;
    raca: string;
    pelagem: string;
    baia: number;
    data_resgate: Date;
    disponivel: boolean;

    constructor(nome: string,idade: number,tipo: string,sexo: string, raca: string
        ,pelagem: string,baia: number, data_resgate: Date,disponivel: boolean
    )
    {
        this.nome=nome;
        this.idade=idade;
        this.tipo=tipo;
        this.sexo=sexo;
        this.raca=raca;
        this.pelagem=pelagem;
        this.baia=baia;
        this.data_resgate=data_resgate;
        this.disponivel=disponivel;
    }
}