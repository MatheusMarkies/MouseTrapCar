package com.matheusmarkies.manager.utilities;

public class KalmanFilter {
    private double x; // estado do filtro
    private double p; // incerteza do estado
    private double q; // incerteza do processo
    private double r; // incerteza da medição

    public KalmanFilter(double x, double p, double q, double r) {
        this.x = x;
        this.p = p;
        this.q = q;
        this.r = r;
    }

    /**
     * Atualiza o filtro de Kalman com uma nova medição.
     *
     * @param measurement Nova medição
     * @return Novo valor do estado após atualização
     */
    public double update(double measurement) {
        // Previsão do estado e da incerteza
        double xPredicted = x;
        double pPredicted = p + q;

        // Ganho de Kalman
        double k = pPredicted / (pPredicted + r);

        // Atualização do estado e da incerteza
        x = xPredicted + k * (measurement - xPredicted);
        p = (1 - k) * pPredicted;

        return x;
    }
}
