package src.business;

public enum ServicoTipo {
    // Serviços Universais
    Checkup,
    MudançaOleoTravoes,
    SubstituicaoCalcosTravoes,
    SubstituicaoInjetores,
    AlinhamentoDirecao,
    CalibragemRodas,
    SubstituicaoFiltroDeArDaCabine,
    SubstituicaoPneus,
    LimpezaInterior,
    LimpezaExterior,

    // Serviços para Motores a Combustão (Gasóleo ou Gasolina)
    SubstituicaoFiltroCombustivel,
    SubstituicaoConversorCatalitico,
    SubstituicaoBateriaArranque,
    SubstituicaoFiltroAr,
    SubstituicaoFiltroOleo,
    MudancaOleoMotor,

    // Serviços para Motores a Gasóleo (em específico)
    SubstituicaoVelasIncandescencia,
    RegeneracaoFiltroParticulas,
    SubstituicaoFiltroParticulas,

    // Serviços para Motores a Gasolina (em específico)
    SubstituicaoVelasIgnicao,
    SubstituicaoBorboleta,

    // Serviços para Motores Elétricos
    SubstituicaoBateria,
    AvaliacaoDesempenhoBateria
}
