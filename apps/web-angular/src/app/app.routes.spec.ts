import { routes } from './app.routes';

describe('routes', () => {
  it('defines routes for every primary navigation item', () => {
    const routePaths = routes.map((route) => route.path);

    expect(routePaths).toContain('dashboard');
    expect(routePaths).toContain('tournaments');
    expect(routePaths).toContain('players');
    expect(routePaths).toContain('matches');
    expect(routePaths).toContain('standings');
  });
});
