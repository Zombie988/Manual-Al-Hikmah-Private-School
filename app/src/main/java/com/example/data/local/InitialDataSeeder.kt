package com.example.data.local

import com.example.data.model.BehaviorRecordEntity
import com.example.data.model.LearningProgressEntity
import com.example.data.model.SideNoteEntity
import com.example.data.model.StudentEntity
import com.example.data.model.StudentEvaluationEntity

object InitialDataSeeder {

    suspend fun seedDatabaseIfEmpty(dao: SchoolDao) {
        val thirdCount = dao.getThirdLevelCount()
        val firstCount = dao.getFirstGradeCount()

        if (thirdCount == 0) {
            seedThirdLevel(dao)
        }

        if (firstCount == 0) {
            seedFirstGrade(dao)
        }
    }

    private suspend fun seedThirdLevel(dao: SchoolDao) {
        // 1. Level 3 (KG3 / الفئة الثالثة - شعبة النور)
        val rawStudents = listOf(
            Triple("آدم البارد", "Adam Al-Bared", "+963 944 112 233"),
            Triple("آدم البعلبكي", "Adam Al-Baalbaki", "+963 933 223 344"),
            Triple("أدهم المصري", "Adham Al-Masri", "+963 955 334 455"),
            Triple("أسامة السبع بحرات", "Osama Al-Sabbagh", "+963 944 445 566"),
            Triple("جود عميش", "Joud Ameesh", "+963 988 556 677"),
            Triple("جودي جوهرة", "Joudi Jawhara", "+963 999 667 788"),
            Triple("جولي زيتون", "Jolie Zaytoun", "+963 944 778 899"),
            Triple("حذيفة شام عجاج", "Houzaifa Sham Ajaj", "+963 933 889 900"),
            Triple("عامر قريسم", "Amer Qreisem", "+963 955 990 011"),
            Triple("ريان الحلواني", "Rayan Al-Halawani", "+963 988 112 244"),
            Triple("سلطان الحريري", "Sultan Al-Hariri", "+963 944 223 355"),
            Triple("كريم المؤذن", "Karim Al-Moazzen", "+963 933 334 466"),
            Triple("منى صباغ", "Mona Sabbagh", "+963 999 445 577")
        )

        val colors = listOf("#1E3A8A", "#0F766E", "#B45309", "#4338CA", "#047857", "#BE123C", "#0284C7")

        val studentEntities = rawStudents.mapIndexed { index, (arName, enName, phone) ->
            StudentEntity(
                studentCode = "MH-KG3-${String.format("%02d", index + 1)}",
                fullNameArabic = arName,
                fullNameEnglish = enName,
                gradeLevel = "الفئة الثالثة (شعبة النور)",
                guardianName = "ولي أمر الطالب $arName",
                guardianPhone = phone,
                attendanceStatus = if (index == 6) "LATE" else "PRESENT",
                avatarColorHex = colors[index % colors.size]
            )
        }

        val studentIds = dao.insertStudents(studentEntities)

        val evaluations = listOf(
            // 0: آدم البارد
            StudentEvaluationEntity(
                studentId = studentIds[0],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "متوسط",
                handwriting = "متوسط",
                writingSpeed = "ضعيف",
                pronunciation = "جيد",
                isWithdrawn = true,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "متابعة النطق وتحسين سرعة الكتابة في البيت.",
                isAcknowledgedByParent = true,
                parentReply = "تم الاطلاع ونقوم بالتدرب معه يومياً في البيت."
            ),
            // 1: آدم البعلبكي
            StudentEvaluationEntity(
                studentId = studentIds[1],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "طالب متميز جداً ومتعاون وخطه جميل.",
                isAcknowledgedByParent = true,
                parentReply = "شكراً جزيلاً لجهود المعلمة غادة وإدارة المدرسة."
            ),
            // 2: أدهم المصري
            StudentEvaluationEntity(
                studentId = studentIds[2],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "متوسط",
                handwriting = "متوسط",
                writingSpeed = "متوسط",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "مستواه مستقر ومشاركته جيدة بالصف."
            ),
            // 3: أسامة السبع بحرات
            StudentEvaluationEntity(
                studentId = studentIds[3],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "متوسط",
                handwriting = "متوسط",
                writingSpeed = "متوسط",
                pronunciation = "جيد",
                isWithdrawn = true,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "يوجد حالة انفصال بين الأهل، بحاجة لدعم نفسي دائم وتشجيع.",
                hasFamilySeparation = true
            ),
            // 4: جود عميش
            StudentEvaluationEntity(
                studentId = studentIds[4],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = true,
                isAggressive = false,
                isHesitant = true,
                isPolite = true,
                isEasilyBored = false,
                isSlow = true,
                specialNotes = "يحب الألعاب، يرسل للمستمع أطعمة ولا يأكلها بالفرصة."
            ),
            // 5: جودي جوهرة
            StudentEvaluationEntity(
                studentId = studentIds[5],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "ملتزمة وهادئة وتشارك في جميع الأنشطة الصفية."
            ),
            // 6: جولي زيتون
            StudentEvaluationEntity(
                studentId = studentIds[6],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "ضعيف",
                handwriting = "ضعيف",
                writingSpeed = "ضعيف",
                pronunciation = "جيد",
                isWithdrawn = true,
                isAggressive = false,
                isHesitant = true,
                isPolite = true,
                isEasilyBored = false,
                isSlow = true,
                specialNotes = "دخول حمام متكرر - يرجى الاطمئنان والمتابعة الصحية.",
                hasFrequentRestroom = true,
                hasHealthCondition = true
            ),
            // 7: حذيفة شام عجاج
            StudentEvaluationEntity(
                studentId = studentIds[7],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "متوسط",
                handwriting = "متوسط",
                writingSpeed = "متوسط",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "حالة انفصال بين الأب والأم، يتطلب رعاية خاصة واهتماماً مضاعفاً.",
                hasFamilySeparation = true
            ),
            // 8: عامر قريسم
            StudentEvaluationEntity(
                studentId = studentIds[8],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = true,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "لا يقبل دخول الحمام إلا بحضور والدته، بكاء شديد عند الانصراف للأهل.",
                hasFrequentRestroom = true
            ),
            // 9: ريان الحلواني
            StudentEvaluationEntity(
                studentId = studentIds[9],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "-",
                writing = "-",
                handwriting = "ضعيف",
                writingSpeed = "ضعيف",
                pronunciation = "ضعيف",
                isWithdrawn = true,
                isAggressive = false,
                isHesitant = true,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "عمره أصغر من الفئة الثانية، يحتاج تقوية في التأسيس."
            ),
            // 10: سلطان الحريري
            StudentEvaluationEntity(
                studentId = studentIds[10],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "انفصال الأب عن الأم ويعيش مع الأب فقط، معنوياته جيدة ومتحمس.",
                hasFamilySeparation = true
            ),
            // 11: كريم المؤذن
            StudentEvaluationEntity(
                studentId = studentIds[11],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "ضعيف",
                handwriting = "ضعيف",
                writingSpeed = "ضعيف",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = true,
                isPolite = true,
                isEasilyBored = true,
                isSlow = false,
                specialNotes = "يريد كل يوم أن يذهب للبيت وتجلس أمه معه بالصف حتى يهدأ وبعدها تغادر."
            ),
            // 12: منى صباغ
            StudentEvaluationEntity(
                studentId = studentIds[12],
                subject = "اللغة العربية",
                gradeLevel = "الفئة الثالثة",
                teacherName = "أ. غادة الرفاعي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "متوسط",
                writingSpeed = "متوسط",
                pronunciation = "متوسط",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "بكاء شديد عند وضع وجبة الطعام، يرجى تشجيعها في البيت."
            )
        )
        dao.insertEvaluations(evaluations)

        val sideNotesList = listOf(
            SideNoteEntity(
                studentId = studentIds[0],
                teacherName = "أ. غادة الرفاعي (اللغة العربية)",
                subject = "الناحية التعليمية - سرعة الكتابة",
                category = "ACADEMIC",
                noteText = "آدم يقرأ بشكل جيد جداً ولديه نطق سليم، نرجو تدريبه على إملاء أسرع في البيت لتحسين سرعة الكتابة.",
                isUrgent = false,
                isAcknowledgedByParent = true,
                parentResponse = "تم الاطلاع، نقوم بتدريبه يومياً على دفتر الخط المنزلي."
            ),
            SideNoteEntity(
                studentId = studentIds[3],
                teacherName = "المشرفة النفسية والتربوية",
                subject = "الناحية الاجتماعية والنفسية",
                category = "BEHAVIORAL",
                noteText = "نظراً لظروف الانفصال العائلي، نتابع أسامة برعاية واهتمام يومي ليظل مندمجاً مع زملائه ومؤدباً كعادته.",
                isUrgent = true,
                isAcknowledgedByParent = false,
                parentResponse = null
            ),
            SideNoteEntity(
                studentId = studentIds[6],
                teacherName = "المشرفة الصحية - مدرسة منهل الحكمة",
                subject = "ملاحظة صحية خاصة",
                category = "HEALTH",
                noteText = "لوحظ دخول متكرر للحمام خلال الحصص الدراسية، يرجى التكرم بإجراء فحص روتيني للاطمئنان وتزويدنا بأي إرشادات.",
                isUrgent = true,
                isAcknowledgedByParent = false,
                parentResponse = null
            ),
            SideNoteEntity(
                studentId = studentIds[8],
                teacherName = "أ. غادة الرفاعي",
                subject = "الانضباط والتكيف الصفي",
                category = "BEHAVIORAL",
                noteText = "عامر متفوق علمياً بالكامل (القراءة والكتابة والنطق جيد)، لكنه يبكي عند انصراف الأهل ولا يدخل الحمام إلا مع والدته. سنعمل سوياً على تعزيز استقلاليته.",
                isUrgent = true,
                isAcknowledgedByParent = false,
                parentResponse = null
            ),
            SideNoteEntity(
                studentId = studentIds[11],
                teacherName = "إدارة مدرسة منهل الحكمة",
                subject = "التكيف الصفي والتعلق بالأم",
                category = "BEHAVIORAL",
                noteText = "كريم يهدأ بعد دقائق من مغادرة الوالدة ويستجيب للمعلمة. نرجو الاستمرار بالتشجيع اللفظي الإيجابي قبل القدوم للمدرسة.",
                isUrgent = false,
                isAcknowledgedByParent = true,
                parentResponse = "شكراً لرحابة صدر المعلمات وتعاونكم الرائع."
            )
        )
        dao.insertSideNotes(sideNotesList)

        val progressList = listOf(
            LearningProgressEntity(
                studentId = studentIds[0],
                subjectCode = "ARABIC",
                subjectTitleArabic = "اللغة العربية",
                subjectTitleEnglish = "Arabic Language",
                assessmentType = "QUIZ_1",
                assessmentTitleArabic = "تقييم القراءة والنطق",
                score = 88f,
                maxScore = 100f,
                topicArabic = "قراءة النص المشكول وضبط مخارج الحروف",
                topicEnglish = "Reading & Pronunciation",
                teacherNotes = "نطق ممتاز، يحتاج متابعة في سرعة نقل الكلمات."
            ),
            LearningProgressEntity(
                studentId = studentIds[1],
                subjectCode = "ARABIC",
                subjectTitleArabic = "اللغة العربية",
                subjectTitleEnglish = "Arabic Language",
                assessmentType = "QUIZ_1",
                assessmentTitleArabic = "التقييم الشامل",
                score = 98f,
                maxScore = 100f,
                topicArabic = "القراءة والكتابة والخط المتقن",
                topicEnglish = "Comprehensive Arabic",
                teacherNotes = "ممتاز في جميع البنود، قدوة حسنة في الفصل."
            )
        )
        dao.insertLearningProgressList(progressList)
    }

    private suspend fun seedFirstGrade(dao: SchoolDao) {
        // 2. First Grade (الصف الأول الأساسي - الشعبة الأولى)
        // Verbatim from the Manhal Al-Hikmah 1st Grade official tracking sheets
        val firstGradeStudents = listOf(
            Triple("ليان أبو جيب", "Layan Abu Jaib", "+963 944 556 781"),
            Triple("ليان بالي", "Layan Bali", "+963 933 667 892"),
            Triple("ليليا معتوق قطاع", "Lilia Maatouq Qataa", "+963 955 778 903"),
            Triple("أحمد الكردي", "Ahmad Al-Kurdi", "+963 944 889 014"),
            Triple("إياس النعسان", "Iyas Al-Naassan", "+963 988 990 125"),
            Triple("بانة دعبول", "Bana Daaboul", "+963 999 001 236"),
            Triple("تيم الله خوجة", "Taymullah Khoja", "+963 933 112 347"),
            Triple("جود البيطار", "Joud Al-Bitar", "+963 944 223 458"),
            Triple("حلا العسلي", "Hala Al-Asali", "+963 955 334 569"),
            Triple("حمزة الشيخ", "Hamza Al-Sheikh", "+963 988 445 670"),
            Triple("رغد الشربجي", "Raghad Al-Sharbaji", "+963 999 556 781"),
            Triple("زيد الكيلاني", "Zaid Al-Kilani", "+963 944 667 892"),
            Triple("شام الطباع", "Sham Al-Tabbaa", "+963 933 778 903"),
            Triple("كرم الحلبي", "Karam Al-Halabi", "+963 955 889 014"),
            Triple("كريم البزم", "Karim Al-Bezm", "+963 988 990 125"),
            Triple("لين الرفاعي", "Leen Al-Rifai", "+963 999 112 233"),
            Triple("محمد يمان السيروان", "Mohammad Yaman Al-Seirawan", "+963 944 334 455")
        )

        val colors = listOf("#047857", "#1E3A8A", "#B45309", "#0F766E", "#BE123C", "#4338CA", "#0284C7")

        val studentEntities = firstGradeStudents.mapIndexed { index, (arName, enName, phone) ->
            StudentEntity(
                studentCode = "MH-G1-${String.format("%02d", index + 1)}",
                fullNameArabic = arName,
                fullNameEnglish = enName,
                gradeLevel = "الصف الأول (الشعبة الأولى)",
                guardianName = "ولي أمر الطالب $arName",
                guardianPhone = phone,
                attendanceStatus = if (index == 4) "LATE" else "PRESENT",
                avatarColorHex = colors[index % colors.size],
                birthDate = "2019-09-12"
            )
        }

        val studentIds = dao.insertStudents(studentEntities)

        val evaluations = listOf(
            // 0: ليان أبو جيب (Layan Abu Jaib)
            StudentEvaluationEntity(
                studentId = studentIds[0],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "تلميذة متميزة وهادئة، قراءة متقنة وخط جميل جداً.",
                isAcknowledgedByParent = true,
                parentReply = "شكراً جزيلاً لجهود المعلمة ريم وإدارة المدرسة."
            ),
            // 1: ليان بالي (Layan Bali)
            StudentEvaluationEntity(
                studentId = studentIds[1],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "مشاركة ممتازة في الصف وتفاعل دائم مع المعلمة.",
                isAcknowledgedByParent = true,
                parentReply = "فخورون بها وبمدرستها الرائعة."
            ),
            // 2: ليليا معتوق قطاع (Lilia Maatouq Qataa)
            StudentEvaluationEntity(
                studentId = studentIds[2],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "أداء ممتاز في التهجئة والكتابة، مؤدبة ومتعاونة.",
                isAcknowledgedByParent = true,
                parentReply = "تم الاطلاع، شكراً لمتابعتكم الطيبة."
            ),
            // 3: أحمد الكردي
            StudentEvaluationEntity(
                studentId = studentIds[3],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "متوسط",
                handwriting = "متوسط",
                writingSpeed = "متوسط",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "تلميذ نشيط، يحتاج لمتابعة رسم الحروف على السطر في البيت."
            ),
            // 4: إياس النعسان
            StudentEvaluationEntity(
                studentId = studentIds[4],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "متوسط",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "مستواه مستقر وجيد جداً في القراءة السريعة."
            ),
            // 5: بانة دعبول
            StudentEvaluationEntity(
                studentId = studentIds[5],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "هادئة ومتميزة وتحفظ النصوص والأناشيد بسرعة."
            ),
            // 6: تيم الله خوجة
            StudentEvaluationEntity(
                studentId = studentIds[6],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "متوسط",
                writing = "متوسط",
                handwriting = "متوسط",
                writingSpeed = "بطيء",
                pronunciation = "جيد",
                isWithdrawn = true,
                isAggressive = false,
                isHesitant = true,
                isPolite = true,
                isEasilyBored = false,
                isSlow = true,
                specialNotes = "متردد قليلاً عند الإجابة أمام الزملاء، يرجى تشجيعه لزيادة الثقة بالنفس."
            ),
            // 7: جود البيطار
            StudentEvaluationEntity(
                studentId = studentIds[7],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "متفوق، يحب القراءة التعبيرية ولديه مخارج حروف سليمة."
            ),
            // 8: حلا العسلي
            StudentEvaluationEntity(
                studentId = studentIds[8],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "دفتر الواجبات منظم وخطها أنيق جداً ومتقن."
            ),
            // 9: حمزة الشيخ
            StudentEvaluationEntity(
                studentId = studentIds[9],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "متوسط",
                handwriting = "متوسط",
                writingSpeed = "متوسط",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = true,
                isSlow = false,
                specialNotes = "سريع الملل في الحصص الطويلة، يحتاج لتنويع الأنشطة التحفيزية."
            ),
            // 10: رغد الشربجي
            StudentEvaluationEntity(
                studentId = studentIds[10],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "تلميذة ممتازة ومثابرة ونتائجها مشرفة."
            ),
            // 11: زيد الكيلاني
            StudentEvaluationEntity(
                studentId = studentIds[11],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "متعاون جداً مع زملائه ومؤدب ومهذب."
            ),
            // 12: شام الطباع
            StudentEvaluationEntity(
                studentId = studentIds[12],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "تشارك دوماً في الإذاعة الصباحية، نطق سليم وقراءة جهرية معبرة."
            ),
            // 13: كرم الحلبي
            StudentEvaluationEntity(
                studentId = studentIds[13],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "متوسط",
                writing = "متوسط",
                handwriting = "متوسط",
                writingSpeed = "ضعيف",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = true,
                specialNotes = "بحاجة لتدريب يومي على سرعة نقل الجمل من السبورة للدفتر."
            ),
            // 14: كريم البزم
            StudentEvaluationEntity(
                studentId = studentIds[14],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "متوسط",
                writingSpeed = "متوسط",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "مستوى جيد جداً، نرجو التركيز على التمييز بين الحروف المتشابهة."
            ),
            // 15: لين الرفاعي
            StudentEvaluationEntity(
                studentId = studentIds[15],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "مجتهدة وملتزمة بالواجبات الصفية والمنزلية."
            ),
            // 16: محمد يمان السيروان
            StudentEvaluationEntity(
                studentId = studentIds[16],
                subject = "اللغة العربية",
                gradeLevel = "الصف الأول",
                teacherName = "أ. ريم الحلبي",
                reading = "جيد",
                writing = "جيد",
                handwriting = "جيد",
                writingSpeed = "جيد",
                pronunciation = "جيد",
                isWithdrawn = false,
                isAggressive = false,
                isHesitant = false,
                isPolite = true,
                isEasilyBored = false,
                isSlow = false,
                specialNotes = "طالب متفوق ونموذجي في الانضباط والنظافة والترتيب."
            )
        )
        dao.insertEvaluations(evaluations)

        // Side Notes for First Grade
        val firstGradeSideNotes = listOf(
            SideNoteEntity(
                studentId = studentIds[0], // ليان أبو جيب
                teacherName = "أ. ريم الحلبي (معلمة الصف الأول)",
                subject = "إتقان القراءة والخط الجميل",
                category = "PRAISE",
                noteText = "ليان متفوقة في مادة اللغة العربية، خطها رائع وقراءتها سليمة بدون أي تردد. تستحق كل التشجيع.",
                isUrgent = false,
                isAcknowledgedByParent = true,
                parentResponse = "شكراً جزيلاً لجهودك يا أستاذة ريم وبارك الله فيك."
            ),
            SideNoteEntity(
                studentId = studentIds[1], // ليان بالي
                teacherName = "أ. ريم الحلبي (معلمة الصف الأول)",
                subject = "المشاركة الصفية الفعالة",
                category = "PRAISE",
                noteText = "ليان مشاركة ومتحمسة دوماً في الفصل الدراسي وتجيب بدقة وسرعة.",
                isUrgent = false,
                isAcknowledgedByParent = true,
                parentResponse = "شكراً لحسن رعايتكم واهتمامكم بها."
            ),
            SideNoteEntity(
                studentId = studentIds[2], // ليليا معتوق قطاع
                teacherName = "أ. ريم الحلبي (معلمة الصف الأول)",
                subject = "التهجئة السليمة والترتيب",
                category = "ACADEMIC",
                noteText = "ليليا تبدي تقدماً ممتازاً في كتابة الكلمات الثلاثية المشكولة والتهجئة الصحيحة.",
                isUrgent = false,
                isAcknowledgedByParent = true,
                parentResponse = "الحمد لله ونحن نتابع معها دفتر القراءة يومياً."
            ),
            SideNoteEntity(
                studentId = studentIds[6], // تيم الله خوجة
                teacherName = "أ. ريم الحلبي",
                subject = "المتابعة والتشجيع الشفهي",
                category = "BEHAVIORAL",
                noteText = "تيم الله طالب مهذب ومحبوب، نرجو تحفيزه في البيت على القراءة بصوت مسموع لتعزيز ثقته.",
                isUrgent = false,
                isAcknowledgedByParent = false,
                parentResponse = null
            ),
            SideNoteEntity(
                studentId = studentIds[13], // كرم الحلبي
                teacherName = "أ. ريم الحلبي",
                subject = "سرعة نقل الكلمات والسطر",
                category = "ACADEMIC",
                noteText = "كرم ذكي ومجتهد، لكنه بطيء في نقل الجمل من السبورة، نرجو كتابة سطرين يومياً على الدفتر المنزلي لتسريع الكتابة.",
                isUrgent = true,
                isAcknowledgedByParent = false,
                parentResponse = null
            )
        )
        dao.insertSideNotes(firstGradeSideNotes)

        // Progress List for First Grade
        val firstGradeProgress = listOf(
            LearningProgressEntity(
                studentId = studentIds[0],
                subjectCode = "ARABIC",
                subjectTitleArabic = "اللغة العربية",
                subjectTitleEnglish = "Arabic Language",
                assessmentType = "QUIZ_1",
                assessmentTitleArabic = "مذاكرة القراءة والإملاء",
                score = 100f,
                maxScore = 100f,
                topicArabic = "قراءة الحروف بحركاتها وكتابة الكلمات البسيطة",
                topicEnglish = "Reading & Spelling",
                teacherNotes = "علامة كاملة، ممتازة جداً."
            ),
            LearningProgressEntity(
                studentId = studentIds[1],
                subjectCode = "ARABIC",
                subjectTitleArabic = "اللغة العربية",
                subjectTitleEnglish = "Arabic Language",
                assessmentType = "QUIZ_1",
                assessmentTitleArabic = "مذاكرة القراءة والإملاء",
                score = 98f,
                maxScore = 100f,
                topicArabic = "تجريد الحروف وقراءة الجمل القصيرة",
                topicEnglish = "Letter Recognition & Short Sentences",
                teacherNotes = "أداء رائع وتركيز عالي."
            ),
            LearningProgressEntity(
                studentId = studentIds[2],
                subjectCode = "ARABIC",
                subjectTitleArabic = "اللغة العربية",
                subjectTitleEnglish = "Arabic Language",
                assessmentType = "QUIZ_1",
                assessmentTitleArabic = "مذاكرة القراءة والإملاء",
                score = 97f,
                maxScore = 100f,
                topicArabic = "تحليل وتركيب الكلمات والخط",
                topicEnglish = "Word Synthesis & Handwriting",
                teacherNotes = "إتقان ممتاز وخط جميل."
            )
        )
        dao.insertLearningProgressList(firstGradeProgress)
    }
}
