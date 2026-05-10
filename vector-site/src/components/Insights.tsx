const posts = [
  {
    date: '17 April 2026',
    datetime: '2026-04-17',
    title: 'The heat is on: limited-run vector packs for seasonal live events',
    excerpt:
      'How we structure symbol sets, swappable motifs, and QA sheets when a partner asks for a mall-wide takeover in under ten days.',
  },
  {
    date: '07 November 2025',
    datetime: '2025-11-07',
    title: 'From film key art to in-experience UI without losing the magic',
    excerpt:
      'Translating cinematic poster energy into flat paths: hierarchy, negative space, and the three-pixel rule for mobile legibility.',
  },
  {
    date: '22 August 2025',
    datetime: '2025-08-22',
    title: 'Telemetry that reads at 200 km/h',
    excerpt:
      'HUD glyphs for racing titles: stroke parity, glow-less clarity, and batch exports that respect per-platform gamma.',
  },
] as const

export function Insights() {
  return (
    <section className="section" id="insights" aria-labelledby="insights-heading">
      <div className="section__head">
        <h2 id="insights-heading" className="section__title">
          Insights
        </h2>
        <p className="section__intro">
          Editorial beats modeled after a news rail—dated entries, confident headlines, and a single
          sentence of context.
        </p>
      </div>
      <ul className="news-list">
        {posts.map(({ date, datetime, title, excerpt }) => (
          <li key={title}>
            <article className="news-item">
              <time dateTime={datetime}>{date}</time>
              <div>
                <h3>
                  <a href="#top">{title}</a>
                </h3>
                <p>{excerpt}</p>
              </div>
            </article>
          </li>
        ))}
      </ul>
    </section>
  )
}
