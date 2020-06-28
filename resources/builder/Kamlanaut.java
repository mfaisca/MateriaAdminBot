package Unit;
import com.materiabot.GameElements.Ability;
import com.materiabot.GameElements.Ability.Type;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Kamlanaut extends _Default{
	public Kamlanaut() {
		super("Kam'lanaut", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam", "kam");
	}
	
	@Override
	public List<Ability> getAbility(Type type) {
		int[] ids = new int[0];
		switch(type) {
			case BRV:
				ids = new int[]{10347};
				break;
			case HP:
				ids = new int[]{10243, 10242};
				break;
			case S1:
				ids = new int[]{6967};
				break;
			case S2:
				ids = new int[]{6726, 6725};
				break;
			case EX:
				ids = new int[]{6754, 6753};
				break;
			case AA:
				ids = new int[]{6758};
				break;
			case LD:
				ids = new int[]{10015};
				break;
			case BT:
				ids = new int[]{};
				break;
			case CA:
				ids = new int[]{};
				break;
		}
		if(ids.length == 0)
			return super.getAbility(type);
		else
			return IntStream.of(ids).boxed().map(i -> this.getSpecificAbility(i)).collect(Collectors.toList());
	}
}