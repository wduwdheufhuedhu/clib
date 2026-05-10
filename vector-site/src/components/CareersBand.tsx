export function CareersBand() {
  return (
    <section className="careers" id="careers" aria-labelledby="careers-heading">
      <div className="careers__inner">
        <div>
          <p className="eyebrow eyebrow--on-dark">We are hiring</p>
          <h2 id="careers-heading" className="careers__title">
            Join us in shaping the surfaces millions touch every day.
          </h2>
          <p className="careers__copy">
            Your precision fuels our mission: illustration that stays sharp from app icon to stadium
            screen. Remote-first collective, async reviews, obsessive about vectors that engineers
            love to implement.
          </p>
        </div>
        <div className="careers__cta">
          <a className="btn btn--inverted" href="mailto:studio@example.com">
            View open roles
          </a>
          <p className="careers__note">Art direction · senior illustrators · design technologists</p>
        </div>
      </div>
    </section>
  )
}
