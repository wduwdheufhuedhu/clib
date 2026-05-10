const services = [
  {
    name: 'Illustration systems',
    text: 'Grid, stroke, and corner-radius contracts so every asset feels like one family across web, mobile, and print.',
  },
  {
    name: 'Brand marks & wordmarks',
    text: 'Vector-first identities with optical corrections for small sizes and high-contrast environments.',
  },
  {
    name: 'Motion-ready SVG',
    text: 'Layered paths, naming, and viewBox discipline for Lottie, Rive, or engine-native animation.',
  },
  {
    name: 'Technical art direction',
    text: 'Partner reviews, accessibility contrast checks, and export pipelines that keep engineering unblocked.',
  },
] as const

export function Services() {
  return (
    <section className="section section--line" id="services" aria-labelledby="services-heading">
      <div className="section__head section__head--row">
        <h2 id="services-heading" className="section__title">
          Services
        </h2>
        <p className="section__intro section__intro--narrow">
          The same clarity you expect from a modern entertainment company site—capabilities stated
          plainly, no ornament for its own sake.
        </p>
      </div>
      <ul className="service-list">
        {services.map(({ name, text }) => (
          <li key={name} className="service-list__item">
            <h3>{name}</h3>
            <p>{text}</p>
          </li>
        ))}
      </ul>
    </section>
  )
}
