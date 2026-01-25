const API_BASE = 'http://localhost:9090/watche';

export async function fetchDashboardData() {
  const [metricsRes, unusedRes, recentRes] = await Promise.all([
    fetch(`${API_BASE}/metrics?actuatorUrl=http://localhost:8080/actuator`),
    fetch(`${API_BASE}/unused?actuatorUrl=http://localhost:8080/actuator`),
    fetch(`${API_BASE}/lastmins`)
  ]);

  const metricsData = await metricsRes.json();
  const unusedData = await unusedRes.json();
  const recentData = await recentRes.json();

  // Transform metrics into flat endpoints array
  const allEndpoints = [];
  Object.entries(metricsData).forEach(([controller, endpoints]) => {
    endpoints.forEach(ep => {
      allEndpoints.push({ ...ep, controller });
    });
  });

  return {
    endpoints: allEndpoints,
    metrics: metricsData,
    unused: unusedData,
    recent: recentData
  };
}