package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.SavedArticleEntity
import com.example.model.NewsArticle
import com.example.model.NewsCategory
import com.example.model.NewsSource
import com.example.model.ReadingStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class NewsRepository(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val savedDao = database.savedArticleDao()

    private val _readingStats = MutableStateFlow(
        ReadingStats(
            articlesReadToday = 4,
            readingStreakDays = 7,
            totalReadingMinutes = 58,
            topicsExploredCount = 5,
            savedCount = 0
        )
    )
    val readingStats: Flow<ReadingStats> = _readingStats.asStateFlow()

    private val sources = listOf(
        NewsSource("reuters", "Reuters Wire", "R", "https://reuters.com"),
        NewsSource("bbc", "BBC World News", "B", "https://bbc.com"),
        NewsSource("mit_tech", "MIT Tech Review", "M", "https://technologyreview.com"),
        NewsSource("bloomberg", "Bloomberg Markets", "B", "https://bloomberg.com"),
        NewsSource("wired", "Wired Global", "W", "https://wired.com"),
        NewsSource("nature", "Nature Science", "N", "https://nature.com"),
        NewsSource("national_geo", "National Geographic", "G", "https://nationalgeographic.com")
    )

    private val initialArticles = listOf(
        NewsArticle(
            id = "news_1",
            title = "Next-Generation Quantum Network Achieves Record 500-Kilometer Entanglement",
            summary = "International consortium of physicists successfully transmits entangled photon qubits through standard telecommunication fiber optic cables with 99.4% fidelity.",
            content = listOf(
                "GENEVA — In what researchers are describing as a foundational leap for the quantum internet, an international team of optical physicists announced the successful distribution of entangled photon states across a 502-kilometer subterranean fiber route spanning three European research hubs.",
                "The breakthrough, published simultaneously in peer-reviewed physics journals, resolves one of the most stubborn hurdles in quantum communications: signal degradation over terrestrial glass fiber without relying on vulnerable digital repeaters.",
                "\"For the past decade, quantum memory buffers suffered from thermal noise that wiped out quantum spin states within milliseconds,\" explained Dr. Elena Vance, lead investigator at the European Quantum Alliance. \"By integrating cryogenically stabilized diamond color centers, we achieved a sustained coherence time exceeding four seconds at room-adjacent coupling temperatures.\"",
                "The practical implications extend far beyond theoretical mathematics. Financial institutions and government cryptographic networks could deploy quantum key distribution (QKD) across inter-city distances, rendering intercepted messages mathematically impossible to decrypt without alerting the communicating endpoints.",
                "Global telecom providers have already initiated pilot tests to determine how quickly these quantum optical repeaters can be integrated alongside existing 400G and 800G commercial data conduits."
            ),
            keyTakeaways = listOf(
                "Entangled photons transmitted across 502 km of fiber with 99.4% coherence fidelity.",
                "Utilizes novel cryogenic diamond color centers to eliminate classic repeater noise.",
                "Paves the way for unhackable quantum key distribution between major metropolitan areas.",
                "Commercial telecom trials scheduled to begin within the next fiscal quarter."
            ),
            category = NewsCategory.TECH,
            source = sources[2], // MIT Tech
            author = "Dr. Marcus Chen",
            publishedTimeAgo = "18m ago",
            readTimeMinutes = 4,
            imageDrawableRes = R.drawable.news_hero_banner_1794235146606,
            tags = listOf("Quantum", "Physics", "Cybersecurity", "FiberOptics"),
            isBreaking = true
        ),
        NewsArticle(
            id = "news_2",
            title = "Global Clean Energy Surpasses 40% of Total Grid Generation for First Quarter in History",
            summary = "Accelerated solar and offshore wind installations across North America, Europe, and Asia push renewable generation to historic high, depressing thermal peak loads.",
            content = listOf(
                "BRUSSELS — For the first time in modern industrial history, zero-carbon renewable energy accounted for over 40% of electricity delivered to global power grids in the preceding quarter, according to the comprehensive International Energy Intelligence benchmark report.",
                "Massive utility-scale solar arrays across sunny latitudes, coupled with new ultra-deep offshore floating wind platforms in the North Sea and East China Sea, contributed to an unprecedented surge in generation capacity.",
                "Grid operators also highlighted the transformative role of high-capacity battery energy storage systems (BESS). With over 120 gigawatt-hours of grid-scale lithium-iron-phosphate and sodium-ion batteries brought online in 2025 and 2026, renewable curtailment plummeted by 64% compared to previous years.",
                "\"The traditional narrative that renewables cannot sustain base load demand is rapidly expiring in the face of modern battery analytics,\" said chief grid analyst Sarah Lindqvist. Several industrial regions experienced negative wholesale electricity spot prices during midday peak solar output, spurring energy-intensive green hydrogen production."
            ),
            keyTakeaways = listOf(
                "Renewables accounted for 41.2% of global electrical supply in Q1.",
                "Offshore floating wind and large utility solar farms were the primary growth drivers.",
                "Grid-scale sodium-ion and LFP battery deployments reduced wasted curtailment by 64%.",
                "Midday surplus power is rapidly accelerating green hydrogen and industrial electrification."
            ),
            category = NewsCategory.CLIMATE,
            source = sources[0], // Reuters
            author = "Claire Moreau",
            publishedTimeAgo = "1h ago",
            readTimeMinutes = 3,
            imageUrl = null,
            tags = listOf("CleanEnergy", "Solar", "WindPower", "GridStorage"),
            isBreaking = false
        ),
        NewsArticle(
            id = "news_3",
            title = "Autonomous Cargo Vessels Clear Maiden Trans-Pacific Voyage Without Crew Intervention",
            summary = "A 4,000-TEU container ship completed the 5,200-nautical-mile journey between Yokohama and Long Beach using vision-radar sensor fusion and satellite navigation.",
            content = listOf(
                "LONG BEACH — Marine logistics entered a new era this morning as the cargo freighter Pacific Horizon slipped into Berth 48 at the Port of Long Beach. Throughout its 11-day Pacific crossing, the vessel's propulsion, helm, and collision-avoidance systems were directed entirely by onboard autonomous maritime navigation systems.",
                "A remote operations center in Tokyo maintained high-speed low-earth-orbit satellite telemetry with the vessel, monitoring hull stresses, fuel optimization algorithms, and maritime traffic within a 50-mile radius.",
                "According to maritime safety telemetry, the automated vessel adjusted course three times to skirt approaching Pacific storm systems, saving an estimated 14% in heavy fuel oil while maintaining a strict delivery timeline.",
                "Port authorities confirmed that harbor pilots boarded the vessel outside the breakwater to handle final docking in accordance with local maritime port regulations, though harbor maneuvers were reported as impeccably smooth."
            ),
            keyTakeaways = listOf(
                "Vessel completed 5,200 nautical miles without manual steering or engine throttle adjustments.",
                "Dynamic meteorological routing yielded a 14% fuel reduction compared to static navigation lines.",
                "Constant redundant dual-satellite telemetry linked ship to remote control watch in Tokyo.",
                "Paves the way for reduced crew fatigue on long-haul bulk shipping corridors."
            ),
            category = NewsCategory.BUSINESS,
            source = sources[3], // Bloomberg
            author = "Kenji Sato",
            publishedTimeAgo = "2h ago",
            readTimeMinutes = 5,
            imageUrl = null,
            tags = listOf("Maritime", "Logistics", "Autonomy", "Shipping"),
            isBreaking = false
        ),
        NewsArticle(
            id = "news_4",
            title = "James Webb Telescope Discovers Atmospheric Water Vapor on Habitable-Zone Super-Earth",
            summary = "Spectroscopic data confirms clouds and methane-water equilibrium on exoplanet TOI-715b, located 137 light-years away in the constellation Volans.",
            content = listOf(
                "BALTIMORE — Astrophysics teams analyzing deep infrared observations from the James Webb Space Telescope have detected unambiguous spectroscopic signatures of water vapor, carbon dioxide, and atmospheric aerosols on TOI-715b.",
                "The exoplanet is approximately 1.55 times the radius of Earth and orbits safely inside the habitable zone of its host red dwarf star, completing an orbit every 19.2 Earth days.",
                "\"What makes TOI-715b uniquely captivating is that its atmospheric pressure and temperature profile appear consistent with liquid water oceans under a thick cloud canopy,\" remarked Dr. Aris Thorne of the Space Telescope Science Institute.",
                "Further observations scheduled for later this year will test for secondary biosignatures, including potential atmospheric ozone and dimethyl sulfide, which would offer clues into organic processes or active hydrothermal venting."
            ),
            keyTakeaways = listOf(
                "Clear spectral absorption peaks for H2O and CO2 confirmed on TOI-715b.",
                "Planet sits securely within the circumstellar habitable zone of a calm M-dwarf star.",
                "Atmospheric modeling suggests cloud layers and possible surface liquid water.",
                "Targeted follow-up spectroscopy slated for late autumn to measure biosignature candidates."
            ),
            category = NewsCategory.SCIENCE,
            source = sources[5], // Nature
            author = "Dr. Aris Thorne",
            publishedTimeAgo = "3h ago",
            readTimeMinutes = 4,
            imageUrl = null,
            tags = listOf("Astronomy", "JWST", "Exoplanets", "Astrobiology"),
            isBreaking = true
        ),
        NewsArticle(
            id = "news_5",
            title = "Global Treaty on Open-Source AI Architecture Signed by 42 Nations in Kyoto",
            summary = "Accord establishes universal standards for model transparency, synthetic media provenance watermarking, and open academic access to foundational weights.",
            content = listOf(
                "KYOTO — In a landmark diplomatic session concluding a week of intensive negotiations, delegates representing 42 nations and over 80 major research institutes signed the Kyoto Declaration on Responsible and Open Artificial Intelligence.",
                "The treaty safeguards researchers' rights to inspect, audit, and distribute open-weights model weights while enacting strict international protocols prohibiting the deceptive weaponization of synthetic video and audio.",
                "Key to the accord is the requirement that all commercial generative models embed standardized cryptographic provenance signatures in raw bitstreams, allowing operating systems and browsers to authenticate original camera sensor captures from AI-generated imagery without cloud latency.",
                "\"Open scientific inquiry is the best antidote to algorithmic monopoly,\" announced Japan's Minister for Digital Transformation during the closing ceremony. Compliance frameworks will be phased in over 18 months."
            ),
            keyTakeaways = listOf(
                "42 nations commit to protecting open-weights research and public model auditing.",
                "Universal cryptographic hardware watermarks mandated for synthetic media creation.",
                "Creates a shared multilateral compute fund for emerging-market universities.",
                "Sets an 18-month harmonized timeline for national regulatory standards."
            ),
            category = NewsCategory.WORLD,
            source = sources[1], // BBC World
            author = "Julian Sterling",
            publishedTimeAgo = "4h ago",
            readTimeMinutes = 5,
            imageUrl = null,
            tags = listOf("Diplomacy", "ArtificialIntelligence", "KyotoTreaty", "Governance"),
            isBreaking = false
        ),
        NewsArticle(
            id = "news_6",
            title = "The Architecture of Silence: Why Urban Planners Are Designing Sound-Absorbing City Parks",
            summary = "From micro-perforated acoustic timber canopies to babbling water acoustic masks, metropolitan designers are treating acoustic quality as critical healthcare infrastructure.",
            content = listOf(
                "COPENHAGEN — Amidst the relentless hum of urban delivery drones, electric buses, and concrete echoes, a new architectural movement is quietly reshaping public green spaces across European and Scandinavian metropolises.",
                "Dubbed 'acoustic landscaping', designers are integrating parabolic stone berms, dense evergreen hedgerows, and specially engineered slotted cedar pavilions capable of deadening high-decibel traffic noise by up to 18 decibels.",
                "Clinical trials conducted by Copenhagen University's Institute of Environmental Psychology demonstrated that 20 minutes spent in an acoustically damped park reduced salivary cortisol by 28% and measurably lowered resting systolic blood pressure.",
                "\"Silence is not simply an absence of noise; it is an active ecological asset,\" notes lead urbanist Freja Møller. Cities including Zurich, Seoul, and Montreal are now incorporating acoustic decibel caps into future park zoning codes."
            ),
            keyTakeaways = listOf(
                "New acoustic park designs reduce ambient city drone noise by up to 18 dB.",
                "Clinical studies correlate 20 minutes in damped parks with a 28% drop in stress hormones.",
                "Architectural techniques combine porous timber, bubbling fountain masks, and dense flora.",
                "Acoustic wellness standards are becoming municipal zoning requirements in top cities."
            ),
            category = NewsCategory.CULTURE,
            source = sources[4], // Wired
            author = "Freja Møller",
            publishedTimeAgo = "5h ago",
            readTimeMinutes = 3,
            imageUrl = null,
            tags = listOf("Architecture", "UrbanPlanning", "Health", "Design"),
            isBreaking = false
        ),
        NewsArticle(
            id = "news_7",
            title = "Revolutionary Solid-State Electrolyte Extends Drone Flight Time to Four Hours",
            summary = "Silicon-anode pouch cells deliver 480 Wh/kg specific energy, enabling long-range medical delivery and disaster reconnaissance missions.",
            content = listOf(
                "SINGAPORE — Drone delivery capabilities may soon expand from short suburban hops to regional delivery corridors following the demonstration of a novel composite solid-state electrolyte battery by deep-tech battery startup Voltronics.",
                "The pouch cells achieved a verified energy density of 480 watt-hours per kilogram—nearly double that of conventional lithium polymer batteries used in commercial quadcopters today.",
                "During a continuous flight endurance test over the Malacca Strait, an unmanned humanitarian cargo drone carrying temperature-sensitive vaccine payloads stayed aloft for 4 hours and 12 minutes before returning with 15% battery reserve.",
                "The ceramic-polymer hybrid separator also passed severe nail-penetration safety tests without thermal runaway, eliminating fire risks during rapid turnaround operations."
            ),
            keyTakeaways = listOf(
                "480 Wh/kg energy density tested under flight-stress conditions.",
                "Unmanned medical delivery drones reach 4+ hours uninterrupted range.",
                "Incombustible solid-state electrolyte passes rigorous puncture and heat stresses.",
                "Production manufacturing tooling scheduled for delivery in late 2026."
            ),
            category = NewsCategory.TECH,
            source = sources[2], // MIT Tech
            author = "Nicholas Tan",
            publishedTimeAgo = "6h ago",
            readTimeMinutes = 3,
            imageUrl = null,
            tags = listOf("Batteries", "Drones", "SolidState", "Aerospace"),
            isBreaking = false
        ),
        NewsArticle(
            id = "news_8",
            title = "Global Central Banks Launch Instant Cross-Border Wholesale Settlement Trial",
            summary = "Ten monetary authorities test multi-currency ledger allowing international trade payments to settle in under three seconds with fractional transaction fees.",
            content = listOf(
                "ZURICH — The Bank for International Settlements (BIS) alongside ten participating central banks has unveiled Project Agora II, a shared programmable ledger intended to modernize wholesale cross-border monetary clearing.",
                "Currently, international wire settlements require multiple correspondent banking intermediaries, taking anywhere from 24 to 72 hours and incurring substantial currency exchange and compliance frictions.",
                "Under the new pilot, commercial banks transacted over 8,000 corporate cross-currency invoices instantaneously using tokenized central bank liabilities. Average settlement latency dropped from 36 hours to 2.4 seconds.",
                "\"This is not speculative cryptocurrency; it is the modernization of core international trade finance rails,\" stated BIS Innovation Hub Director Cecilia Skoglund."
            ),
            keyTakeaways = listOf(
                "10 major central banks participate in Project Agora II programmable settlement.",
                "International wholesale payment execution reduced from days to 2.4 seconds.",
                "Drastically lowers counterparty liquidity risks for exporters and importers.",
                "Uses tokenized reserve deposits rather than unbacked crypto tokens."
            ),
            category = NewsCategory.BUSINESS,
            source = sources[3], // Bloomberg
            author = "Dominic Vance",
            publishedTimeAgo = "7h ago",
            readTimeMinutes = 4,
            imageUrl = null,
            tags = listOf("Finance", "Banking", "Economy", "Fintech"),
            isBreaking = false
        )
    )

    fun getAllSources(): List<NewsSource> = sources

    fun getAllArticles(): List<NewsArticle> = initialArticles

    fun getArticlesByCategory(category: NewsCategory): List<NewsArticle> {
        return if (category == NewsCategory.ALL) {
            initialArticles
        } else {
            initialArticles.filter { it.category == category }
        }
    }

    fun getArticleById(id: String): NewsArticle? {
        return initialArticles.find { it.id == id }
    }

    fun searchArticles(query: String, category: NewsCategory = NewsCategory.ALL): List<NewsArticle> {
        val q = query.trim().lowercase()
        return initialArticles.filter { article ->
            val matchesCategory = (category == NewsCategory.ALL || article.category == category)
            val matchesQuery = q.isEmpty() ||
                article.title.lowercase().contains(q) ||
                article.summary.lowercase().contains(q) ||
                article.source.name.lowercase().contains(q) ||
                article.tags.any { it.lowercase().contains(q) }
            matchesCategory && matchesQuery
        }
    }

    fun getBreakingNews(): List<NewsArticle> {
        return initialArticles.filter { it.isBreaking }
    }

    // Saved/Offline articles Flow
    fun getSavedArticlesFlow(): Flow<List<NewsArticle>> {
        return savedDao.getAllSaved().map { entities ->
            entities.map { it.toNewsArticle() }
        }
    }

    suspend fun isArticleSaved(id: String): Boolean {
        return savedDao.isArticleSaved(id)
    }

    suspend fun toggleSaveArticle(article: NewsArticle): Boolean {
        val alreadySaved = savedDao.isArticleSaved(article.id)
        if (alreadySaved) {
            savedDao.deleteArticleById(article.id)
            _readingStats.value = _readingStats.value.copy(
                savedCount = (_readingStats.value.savedCount - 1).coerceAtLeast(0)
            )
            return false
        } else {
            savedDao.insertArticle(article.toEntity())
            _readingStats.value = _readingStats.value.copy(
                savedCount = _readingStats.value.savedCount + 1
            )
            return true
        }
    }

    suspend fun removeSavedArticle(id: String) {
        savedDao.deleteArticleById(id)
        _readingStats.value = _readingStats.value.copy(
            savedCount = (_readingStats.value.savedCount - 1).coerceAtLeast(0)
        )
    }

    fun recordArticleRead(article: NewsArticle) {
        val current = _readingStats.value
        _readingStats.value = current.copy(
            articlesReadToday = current.articlesReadToday + 1,
            totalReadingMinutes = current.totalReadingMinutes + article.readTimeMinutes
        )
    }

    private fun SavedArticleEntity.toNewsArticle(): NewsArticle {
        val cat = NewsCategory.entries.find { it.name == this.categoryName } ?: NewsCategory.TECH
        val src = sources.find { it.name == this.sourceName } ?: NewsSource(
            id = "src_custom",
            name = this.sourceName,
            initial = this.sourceInitial,
            website = this.sourceWebsite
        )
        return NewsArticle(
            id = this.id,
            title = this.title,
            summary = this.summary,
            content = this.contentRaw.split("\n\n").filter { it.isNotBlank() },
            keyTakeaways = this.keyTakeawaysRaw.split("\n").filter { it.isNotBlank() },
            category = cat,
            source = src,
            author = this.author,
            publishedTimeAgo = this.publishedTimeAgo,
            readTimeMinutes = this.readTimeMinutes,
            imageUrl = this.imageUrl,
            imageDrawableRes = this.imageDrawableRes,
            tags = this.tagsRaw.split(",").map { it.trim() }.filter { it.isNotEmpty() },
            isBreaking = false,
            isSaved = true
        )
    }

    private fun NewsArticle.toEntity(): SavedArticleEntity {
        return SavedArticleEntity(
            id = this.id,
            title = this.title,
            summary = this.summary,
            contentRaw = this.content.joinToString("\n\n"),
            keyTakeawaysRaw = this.keyTakeaways.joinToString("\n"),
            categoryName = this.category.name,
            sourceName = this.source.name,
            sourceInitial = this.source.initial,
            sourceWebsite = this.source.website,
            author = this.author,
            publishedTimeAgo = this.publishedTimeAgo,
            readTimeMinutes = this.readTimeMinutes,
            imageUrl = this.imageUrl,
            imageDrawableRes = this.imageDrawableRes,
            tagsRaw = this.tags.joinToString(","),
            savedAtTimestamp = System.currentTimeMillis()
        )
    }
}
