import express, { request, response } from 'express';
const app = express();
app.use(express.json());

import { Animal } from '../models/Animal';

// import Animal from 
// const animal= new Animal;

const teste: any[] = [];
app.get('/animal', (req,res) => {

    res.json(teste);
});

app.post('/animal',(req,res) => {
    
    teste.push(req.body);
    res.send('ok');
})

app.listen(3000); // porta que ele esta rodando 

// app.post('/animal');

// import { Request, Response } from 'express';
// import {Animal} from '../models/Animal';