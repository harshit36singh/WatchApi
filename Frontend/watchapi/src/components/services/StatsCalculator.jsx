export function calculateControllerStats(metrics) {
  const controllerStats = {};
  
  Object.entries(metrics).forEach(([controller, eps]) => {
    const total = eps.reduce((sum, ep) => sum + ep.metrics.total, 0);
    const avg = eps.reduce((sum, ep) => sum + ep.metrics.avgResponseMs, 0) / eps.length;
    
    controllerStats[controller] = { 
      total, 
      avg, 
      count: eps.length,
      endpoints: eps.sort((a, b) => b.metrics.total - a.metrics.total)
    };
  });

  return controllerStats;
}