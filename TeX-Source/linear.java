private float[] linearRegression(float[] x, float[] y) {
    float sumX = 0;
    float sumY = 0;
    float sumXY = 0;
    float sumXX = 0;
    for (int i = 0; i < x.length; i++) {
        sumX += x[i];
        sumY += y[i];
        sumXY += x[i] * y[i];
        sumXX += x[i] * x[i];
    }
    float a = (sumY * sumXX - sumX * sumXY) / (x.length * sumXX - sumX * sumX);
    float b = (x.length * sumXY - sumX * sumY) / (x.length * sumXX - sumX * sumX);
    return new float[] { a, b };
}